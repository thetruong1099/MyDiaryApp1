package com.example.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiary.R
import com.example.mydiary.model.Diary
import kotlinx.android.synthetic.main.diary_of_date_item.view.*

class DiaryOfDateAdapter(private val onClick:(diary:Diary)->Unit):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listItem:MutableList<Diary> = mutableListOf()

    inner class DiaryOfDateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvTitle = itemView.tv_title
        private val tvContent = itemView.tv_content

        fun onBind(diary: Diary){
            tvTitle.text = diary.title
            tvContent.text = diary.content

            itemView.setOnClickListener { onClick(diary) }
        }
    }

    private class DiaryOfDateViewHolderZero(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.diary_of_date_item,parent,false)
            return DiaryOfDateViewHolder(itemView)
        }
        else{
            val itemView:View = LayoutInflater.from(parent.context).inflate(R.layout.diary_of_date_item_zero, parent, false)
            return DiaryOfDateViewHolderZero(itemView)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DiaryOfDateViewHolder){
            holder.onBind(listItem[position])
        }
    }

    override fun getItemCount(): Int {
        if (listItem.size==0) return 1
        return listItem.size
    }

    override fun getItemViewType(position: Int): Int {
        if (listItem.size == 0) return 0
        return 1
    }

    fun setListItem(listItem:MutableList<Diary>){
        this.listItem = listItem
        notifyDataSetChanged()
    }
}