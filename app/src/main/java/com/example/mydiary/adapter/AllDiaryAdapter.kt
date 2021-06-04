package com.example.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiary.R
import com.example.mydiary.model.Diary
import kotlinx.android.synthetic.main.diary_item.view.*
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*

class AllDiaryAdapter(
    private val onclick: (Diary) -> Unit,
    private val onDeleteClick:(Diary) ->Unit
) : RecyclerView.Adapter<AllDiaryAdapter.ViewHolder>() {

    private var listDiary: MutableList<Diary> = mutableListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTime = itemView.tv_time
        private val tvTitle = itemView.tv_title
        private val tvStory = itemView.tv_story
        private val btnDetele = itemView.btnDelete

        fun onBind(diary: Diary) {
            tvTime.text = formantTime(diary.year, diary.month, diary.date) + diary.time
            tvTitle.text = diary.title
            tvStory.text = diary.content
            itemView.setOnClickListener { onclick(diary) }
            btnDetele.setOnClickListener { onDeleteClick(diary) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.diary_item, parent, false)
        return ViewHolder((itemView))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(listDiary[position])
    }

    override fun getItemCount(): Int = listDiary.size

    private fun formantTime(year: Int, month: Int, date: Int): String {
        val formatDate = SimpleDateFormat("EEEE, dd MMMM YYYY ", Locale.UK)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DATE, date)

        return formatDate.format(calendar.time)
    }

    fun setListDiary(listDiary: MutableList<Diary>) {
        this.listDiary = listDiary
        notifyDataSetChanged()
    }

    fun clearListDiary() {
        listDiary.clear()
        notifyDataSetChanged()
    }
}