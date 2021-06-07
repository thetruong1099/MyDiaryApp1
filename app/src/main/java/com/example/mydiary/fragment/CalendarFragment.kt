package com.example.mydiary.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.mydiary.R
import com.example.mydiary.activity.DetailDiaryActivity
import com.example.mydiary.adapter.CalendarViewPagerAdapter
import com.example.mydiary.adapter.DiaryOfDateAdapter
import com.example.mydiary.model.Diary
import com.example.mydiary.util.CustomBackStackFragment
import com.example.mydiary.util.FormatTime
import com.example.mydiary.viewmodel.DiaryViewModel
import com.example.mydiary.viewmodel.SharedPreferenceViewModel
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : CustomBackStackFragment() {

    private var currentYear = 0
    private var currentMonth = 0
    private var currentDay = 0
    private val size = 1000
    private var dayStart: String = "Sunday"
    private lateinit var adapterViewPagers: CalendarViewPagerAdapter

    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(
            this,
            DiaryViewModel.DiaryViewModelFactory(requireActivity().application)
        )[DiaryViewModel::class.java]
    }

    private val sharePreferenceViewModel by lazy {
        ViewModelProvider(
            this,
            SharedPreferenceViewModel.SharePreferenceViewModelFactory(requireContext())
        )[SharedPreferenceViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onStart() {
        super.onStart()

        //set textview day of week
        getDayStartFromDB()

        //get current year, month, day

        val calendar = Calendar.getInstance()
        currentYear = calendar.get(Calendar.YEAR)
        currentMonth = calendar.get(Calendar.MONTH) + 1
        currentDay = calendar.get(Calendar.DATE)

        tv_year_month.text = formatMonthYear(currentYear, currentMonth)

        var fragmentList = MutableList<Fragment>(size) { index -> Fragment() }
        fragmentList.set(size / 2 - 1, MonthFragment(currentYear, currentMonth - 1))
        fragmentList.set(
            size / 2,
            MonthFragment(currentYear, currentMonth)
        )
        fragmentList.set(size / 2 + 1, MonthFragment(currentYear, currentMonth + 1))

        adapterViewPagers = CalendarViewPagerAdapter(
            fragmentList,
            childFragmentManager,
            lifecycle
        )

        view_pager_calendar.apply {
            adapter = adapterViewPagers
            setCurrentItem(size / 2, false)
        }

        view_pager_calendar.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            var jump = size / 2

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                var month = currentMonth + (position - size / 2)

                tv_year_month.text = formatMonthYear(currentYear, month)

                if (position < jump) {
                    jump = position
                    adapterViewPagers.setFragmentAtPosition(
                        position - 1,
                        MonthFragment(currentYear, month - 1)
                    )

                } else if (position > jump) {
                    jump = position
                    adapterViewPagers.setFragmentAtPosition(
                        position + 1,
                        MonthFragment(currentYear, month + 1)
                    )
                }

                if (position != size / 2) {
                    tv_current_day.visibility = View.VISIBLE
                    tv_current_day.text = currentDay.toString()
                } else {
                    tv_current_day.visibility = View.GONE
                }
            }
        })

        tv_current_day.setOnClickListener { view_pager_calendar.currentItem = size / 2 }

        tv_date_has_diary.text =
            FormatTime.formatDateTime(currentYear, currentMonth, currentDay, "dd MMMM YYYY")

        getDiaryOfCurrentDate(currentYear, currentMonth, currentDay)
    }

    private fun getDiaryOfCurrentDate(year: Int, month: Int, date: Int) {
        diaryViewModel.getDiaryOfDate(year, month, date).observe(this, androidx.lifecycle.Observer {
            it.sortedBy {
                it.time
            }
            setDiaryOfDateRv(it)
        })
    }

    private fun setDiaryOfDateRv(list: MutableList<Diary>) {
        var diaryOfDateAdapter = DiaryOfDateAdapter(onItemClick)

        rv_diary_of_day.apply {
            adapter = diaryOfDateAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        diaryOfDateAdapter.setListItem(list)
    }

    private val onItemClick: (diary: Diary) -> Unit = {
        val intent = Intent(context, DetailDiaryActivity::class.java)
        intent.putExtra("Detail Diary", it)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun formatMonthYear(year: Int, month: Int): String {
        var formatDate = SimpleDateFormat("YYYY MMMM ", Locale.UK)
        val setCalendar = Calendar.getInstance()
        setCalendar.set(year, month - 1, 10)
        return formatDate.format(setCalendar.time)
    }

    private fun getDayStartFromDB() {
        sharePreferenceViewModel.getDayStart().observe(this, androidx.lifecycle.Observer {
            setDayStartTextView(it)
        })
    }

    private fun setDayStartTextView(string: String) {
        when (string) {
            "Monday" -> {
                tv_day_first.text = "MON"
                tv_day_second.text = "TUE"
                tv_day_third.text = "WED"
                tv_day_fourth.text = "THUR"
                tv_day_fifth.text = "FRI"
                tv_day_sixth.text = "SAT"
                tv_day_seventh.text = "SUN"
            }
            "Tuesday" -> {
                tv_day_first.text = "TUE"
                tv_day_second.text = "WED"
                tv_day_third.text = "THUR"
                tv_day_fourth.text = "FRI"
                tv_day_fifth.text = "SAT"
                tv_day_sixth.text = "SUN"
                tv_day_seventh.text = "MON"
            }
            "Wednesday" -> {
                tv_day_first.text = "WED"
                tv_day_second.text = "THUR"
                tv_day_third.text = "FRI"
                tv_day_fourth.text = "SAT"
                tv_day_fifth.text = "SUN"
                tv_day_sixth.text = "MON"
                tv_day_seventh.text = "TUE"
            }
            "Thursday" -> {
                tv_day_first.text = "THUR"
                tv_day_second.text = "FRI"
                tv_day_third.text = "SAT"
                tv_day_fourth.text = "SUN"
                tv_day_fifth.text = "MON"
                tv_day_sixth.text = "TUE"
                tv_day_seventh.text = "WED"
            }
            "Friday" -> {
                tv_day_first.text = "FRI"
                tv_day_second.text = "SAT"
                tv_day_third.text = "SUN"
                tv_day_fourth.text = "MON"
                tv_day_fifth.text = "TUE"
                tv_day_sixth.text = "WED"
                tv_day_seventh.text = "THUR"
            }
            "Saturday" -> {
                tv_day_first.text = "SAT"
                tv_day_second.text = "SUN"
                tv_day_third.text = "MON"
                tv_day_fourth.text = "TUE"
                tv_day_fifth.text = "WED"
                tv_day_sixth.text = "THUR"
                tv_day_seventh.text = "FRI"
            }
            "Sunday" -> {
                tv_day_first.text = "SUN"
                tv_day_second.text = "MON"
                tv_day_third.text = "TUE"
                tv_day_fourth.text = "WED"
                tv_day_fifth.text = "THUR"
                tv_day_sixth.text = "FRI"
                tv_day_seventh.text = "SAT"
            }
        }
    }
}