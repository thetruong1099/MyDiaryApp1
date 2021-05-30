package com.example.mydiary.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiary.R
import com.example.mydiary.util.OnDoubleClickListener
import kotlinx.android.synthetic.main.day_in_month_item.view.*

class DayInMonthAdapter(
    private val onSingleClicl: (MutableMap<String, Int>) -> Unit,
    private val onDoubleClick: (MutableMap<String, Int>) -> Unit
) : RecyclerView.Adapter<DayInMonthAdapter.ViewHolder>() {

    private var items: MutableList<MutableMap<String, Int>> = mutableListOf()
    private var currentDay: Int = 0
    private var listDayHasDiary: MutableList<MutableMap<String, Int>> = mutableListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var date = itemView.tv_cell_day
        private val layoutDayInMonth = itemView.layout_day_in_month

        fun onBind(map: MutableMap<String, Int>) {
            date.text = map["day"].toString()

            if (currentDay == map["day"] && map["dayOfCurrentMonth"] == 1) {
                date.setTextColor(Color.parseColor("#ff7f50"))
            } else if (map["dayOfCurrentMonth"] == 0) {
                    date.setTextColor(Color.parseColor("#80000000"))
            } else date.setTextColor(Color.parseColor("#000000"))

            for (i in listDayHasDiary) {
                if (i["day"] == map["day"] && i["dayOfCurrentMonth"] == map["dayOfCurrentMonth"]) {
                    date.setBackgroundResource(R.drawable.back_ground_item_day_in_month)
                    date.setTextColor(Color.parseColor("#ffffff"))
                }
            }

            itemView.setOnClickListener(object : OnDoubleClickListener() {
                override fun onSingleClick(v: View?) {
                    onSingleClicl(map)
                }

                override fun onDoubleClick(v: View?) {
                    onDoubleClick(map)
                }

            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.day_in_month_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setListItems(items: MutableList<MutableMap<String, Int>>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun setCurrentDay(currentDay: Int) {
        this.currentDay = currentDay
        notifyDataSetChanged()
    }

    fun setListDayHasDiary(listDayHasDiary: MutableList<MutableMap<String, Int>>) {
        this.listDayHasDiary.addAll(listDayHasDiary)
        notifyDataSetChanged()
    }
}