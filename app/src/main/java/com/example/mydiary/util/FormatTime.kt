package com.example.mydiary.util

import java.text.SimpleDateFormat
import java.util.*

class FormatTime {

    companion object{
        fun formatDateTime(year: Int, month: Int, date: Int, stringFormant:String):String{
            var formatDate = SimpleDateFormat(stringFormant, Locale.UK)
            val setCalendar = Calendar.getInstance()
            setCalendar.set(Calendar.YEAR, year)
            setCalendar.set(Calendar.MONTH, month - 1)
            setCalendar.set(Calendar.DATE, date)

            return formatDate.format(setCalendar.time)
        }

        fun formatDateTime(year: Int, month: Int, stringFormant:String):String{
            var formatDate = SimpleDateFormat(stringFormant, Locale.UK)
            val setCalendar = Calendar.getInstance()
            setCalendar.set(Calendar.YEAR, year)
            setCalendar.set(Calendar.MONTH, month - 1)

            return formatDate.format(setCalendar.time)
        }

        fun formatDateTime(stringFormant:String):String{
            var formatDate = SimpleDateFormat(stringFormant, Locale.UK)
            val setCalendar = Calendar.getInstance()

            return formatDate.format(setCalendar.time)
        }
    }
}