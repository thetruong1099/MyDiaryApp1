package com.example.mydiary.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydiary.R
import com.example.mydiary.activity.DetailDiaryActivity
import com.example.mydiary.activity.WriteDiaryActivity
import com.example.mydiary.adapter.DayInMonthAdapter
import com.example.mydiary.adapter.DiaryOfDateAdapter
import com.example.mydiary.model.Diary
import com.example.mydiary.util.FormatTime
import com.example.mydiary.viewmodel.DiaryViewModel
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_month.*
import java.text.SimpleDateFormat
import java.util.*


class MonthFragment(
    private var currentYear: Int,
    private var currentMonth: Int,
    private var currentDay: Int
) : Fragment() {

    private var calendarItems: MutableList<MutableMap<String, Int>> = mutableListOf()
    private lateinit var dayInMonthAdapter: DayInMonthAdapter
    private var dayStart = "Sunday"

    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(
            this,
            DiaryViewModel.DiaryViewModelFactory(requireActivity().application)
        )[DiaryViewModel::class.java]
    }

    private var listDayHasDiary: MutableList<MutableMap<String, Int>> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onStart() {
        super.onStart()

        if (currentMonth <= 0 || currentMonth > 12) {
            var list = formatMonthYear(currentYear, currentMonth)
            currentYear = list[0].toInt()
            currentMonth = convertMonthIsInt(list[1])
        }

        dayInMonthAdapter = DayInMonthAdapter(onItemSingleClick, onItemDoubleClick)
        calendar_recycler_view.apply {
            layoutManager = GridLayoutManager(context, 7, GridLayoutManager.VERTICAL, false)
            adapter = dayInMonthAdapter
        }

        calendarItems.clear()
        calendarItems = setCalendarItems(dayStart)
        dayInMonthAdapter.setListItems(calendarItems)

        dayInMonthAdapter.setCurrentDay(currentDay)

        getDateHaveDiaryByMonth(currentYear, currentMonth)

    }

    private fun setCalendarItems(string: String): MutableList<MutableMap<String, Int>> {
        var list: MutableList<MutableMap<String, Int>> = mutableListOf()
        var index = 0
        when (string) {
            "Monday" -> index = 1
            "Tuesday" -> index = 2
            "Wednesday" -> index = 3
            "Thursday" -> index = 4
            "Friday" -> index = 5
            "Saturday" -> index = 6
            "Sunday" -> index = 0
            else -> index = 0
        }
        //last day of Previous Month

        val calendarPrevious = Calendar.getInstance()
        calendarPrevious.set(Calendar.MONTH, currentMonth - 2)
        val max = calendarPrevious.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendarPrevious.set(Calendar.DAY_OF_MONTH, max)
        val dayOfPreviousMonth = calendarPrevious.get(Calendar.DATE)

        //last day of current month

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, currentMonth - 1)
        val dayOfMonth = calendar.getActualMaximum(Calendar.DATE)

        //get position of day one of current month
        calendar.set(currentYear, currentMonth - 1, 1)
        val dayOneOfWeek = shiftIndexOfWeek(calendar.get(Calendar.DAY_OF_WEEK), index)

        //get Day Of Previous Month
        if (dayOneOfWeek > 1) {
            for (day in dayOfPreviousMonth - dayOneOfWeek + 2..dayOfPreviousMonth) {
                val map = mutableMapOf<String, Int>()
                map["day"] = day
                map["dayOfCurrentMonth"] = 0
                list.add(map)
            }
        }

        //get day  of current month
        var week: Int = 0
        for (day in 1..dayOfMonth) {
            val map = mutableMapOf<String, Int>()
            map["day"] = day
            calendar.set(currentYear, currentMonth - 1, day)
            week = shiftIndexOfWeek(calendar.get(Calendar.DAY_OF_WEEK), index)
            map["dayOfCurrentMonth"] = 1
            list.add(map)
        }

        //get day of next month
        var day = 1
        if (week < 7) {
            for (i in week + 1..7) {
                val map = mutableMapOf<String, Int>()
                map["day"] = day
                day++
                map["dayOfCurrentMonth"] = 0
                list.add(map)
            }
        }
        calendarPrevious.clear()
        calendar.clear()

        return list
    }

    private fun shiftIndexOfWeek(week: Int, index: Int): Int {
        var shiftWeek = week - index
        if (shiftWeek < 1) return 7 + shiftWeek
        return shiftWeek
    }

    private fun formatMonthYear(year: Int, month: Int): List<String> {
        val time = FormatTime.formatDateTime(year, month, "YYYY MMMM ")
        var list: List<String> = time.split(" ")
        return list
    }

    private fun convertMonthIsInt(string: String): Int {
        when (string) {
            "January" -> return 1
            "February" -> return 2
            "March" -> return 3
            "April" -> return 4
            "May" -> return 5
            "June" -> return 6
            "July" -> return 7
            "August" -> return 8
            "September" -> return 9
            "October" -> return 10
            "November" -> return 11
            "December" -> return 12
        }
        return 0
    }

    private fun getDateHaveDiaryByMonth(year: Int, month: Int) {
        //previous month

        var previousYear: Int = 0
        var previousMonth: Int = month - 1
        if (previousMonth != 0) previousYear = year
        else {
            previousYear = year - 1
            previousMonth = 12
        }

        if (calendarItems[0]["dayOfCurrentMonth"] == 0) {
            val limitLeftOfMonth: Int = calendarItems[0]["day"]!!

            diaryViewModel.getDiaryDateByMonth(previousYear, previousMonth)
                .observe(this, androidx.lifecycle.Observer {
                    for (i in it) {
                        var map: MutableMap<String, Int> = mutableMapOf()
                        if (i >= limitLeftOfMonth) {
                            map["day"] = i
                            map["dayOfCurrentMonth"] = 0
                            listDayHasDiary.add(map)
                        }
                    }
                    dayInMonthAdapter.setListDayHasDiary(listDayHasDiary)
                })
        }

        //current month

        diaryViewModel.getDiaryDateByMonth(year, month).observe(this, androidx.lifecycle.Observer {
            for (i in it) {
                var map: MutableMap<String, Int> = mutableMapOf()
                map["day"] = i
                map["dayOfCurrentMonth"] = 1
                listDayHasDiary.add(map)
            }
            dayInMonthAdapter.setListDayHasDiary(listDayHasDiary)
        })

        //next month

        var nextYear = 0
        var nextMonth = month + 1
        if (nextMonth != 13) nextYear = year
        else {
            nextYear = year + 1
            nextMonth = 1
        }
        if (calendarItems[calendarItems.size - 1]["dayOfCurrentMonth"] == 0) {
            val limitRightOfMonth: Int = calendarItems[calendarItems.size - 1]["day"]!!

            diaryViewModel.getDiaryDateByMonth(nextYear, nextMonth)
                .observe(this, androidx.lifecycle.Observer {
                    for (i in it) {
                        var map: MutableMap<String, Int> = mutableMapOf()
                        if (i <= limitRightOfMonth) {
                            map["day"] = i
                            map["dayOfCurrentMonth"] = 0
                            listDayHasDiary.add(map)
                        }
                    }
                    dayInMonthAdapter.setListDayHasDiary(listDayHasDiary)
                })
        }
    }

    private fun getDiaryOfDate(year: Int, month: Int, date: Int) {
        diaryViewModel.getDiaryOfDate(year, month, date).observe(this, androidx.lifecycle.Observer {
            it.sortedBy {
                it.time

            }
            setDiaryOfDateRv(it)
        })
    }

    private fun setDiaryOfDateRv(list: MutableList<Diary>) {
        var diaryOfDateAdapter = DiaryOfDateAdapter(onItemClick)

        requireParentFragment().rv_diary_of_day.apply {
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

    private val onItemSingleClick: (map: MutableMap<String, Int>) -> Unit = {
        var month = 0
        var year: Int = currentYear
        var date: Int = it["day"] as Int
        if (it["dayOfCurrentMonth"] == 1) {
            month = currentMonth
            year = currentYear
        } else {
            if (it["day"] as Int > 20) {
                if (currentMonth - 1 == 0) {
                    month = 12
                    year = currentYear - 1
                } else month = currentMonth - 1
            } else {
                if (currentMonth + 1 == 13) {
                    month = 1
                    year = currentYear + 1
                } else month = currentMonth + 1
            }
        }

        requireParentFragment().tv_date_has_diary.text =
            FormatTime.formatDateTime(year, month, date, "dd MMMM YYYY ")

        getDiaryOfDate(year, month, date)
    }

    private val onItemDoubleClick: (map: MutableMap<String, Int>) -> Unit = {
        var month = 0
        var year: Int = currentYear
        if (it["dayOfCurrentMonth"] == 1) {
            month = currentMonth
            year = currentYear
        } else {
            if (it["day"] as Int > 20) {
                if (currentMonth - 1 == 0) {
                    month = 12
                    year = currentYear - 1
                } else month = currentMonth - 1
            } else {
                if (currentMonth + 1 == 13) {
                    month = 1
                    year = currentYear + 1
                } else month = currentMonth + 1
            }
        }

        var intent = Intent(requireContext(), WriteDiaryActivity::class.java)
        intent.putExtra("year", year)
        intent.putExtra("month", month)
        intent.putExtra("day", it["day"])
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}