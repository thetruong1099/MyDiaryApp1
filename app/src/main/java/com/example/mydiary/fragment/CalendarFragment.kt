package com.example.mydiary.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.mydiary.R
import com.example.mydiary.activity.DetailDiaryActivity
import com.example.mydiary.adapter.CalendarViewPagerAdapter
import com.example.mydiary.adapter.DiaryOfDateAdapter
import com.example.mydiary.model.Diary
import com.example.mydiary.util.FormatTime
import com.example.mydiary.viewmodel.DiaryViewModel
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onStart() {
        super.onStart()

        val calendar = Calendar.getInstance()
        currentYear = calendar.get(Calendar.YEAR)
        currentMonth = calendar.get(Calendar.MONTH) + 1
        currentDay = calendar.get(Calendar.DATE)

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

                tv_year_month.text = formmatMonthYear(currentYear, month)

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

    private fun formmatMonthYear(year: Int, month: Int): String {
        var formatDate = SimpleDateFormat("YYYY MMMM ", Locale.UK)
        val setCalendar = Calendar.getInstance()
        setCalendar.set(year, month - 1, 10)
        return formatDate.format(setCalendar.time)
    }
}