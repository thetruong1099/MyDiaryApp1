package com.example.mydiary.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mydiary.R
import com.example.mydiary.model.Diary
import com.example.mydiary.util.FormatTime
import com.example.mydiary.viewmodel.DiaryViewModel
import kotlinx.android.synthetic.main.activity_detail_diary.*

class DetailDiaryActivity : AppCompatActivity() {

    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(
            this,
            DiaryViewModel.DiaryViewModelFactory(this.application)
        )[DiaryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_diary)
    }

    override fun onStart() {
        super.onStart()

        val diary = intent.getSerializableExtra("Detail Diary") as Diary

        diaryViewModel.getDetailDiary(diary.year, diary.month, diary.date, diary.time).observe(this, Observer {
            tv_time.text = " ${FormatTime.formatDateTime(it.year, it.month, it.date, "EEEE, dd MMMM YYYY")}   ${it.time}"
            tv_title.text = it.title
            tv_story.text = it.content
        })

        btn_back.setOnClickListener { onBackPressed() }

        btn_edit_diary.setOnClickListener {
            val intent = Intent(this, WriteDiaryActivity::class.java)
            intent.putExtra("check", true)
            intent.putExtra("Diary", diary)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}