package com.example.mydiary.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydiary.R
import com.example.mydiary.adapter.AllDiaryAdapter
import com.example.mydiary.model.Diary
import com.example.mydiary.util.VietnameseCharacterUtil
import com.example.mydiary.viewmodel.DiaryViewModel
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(
            this,
            DiaryViewModel.DiaryViewModelFactory(this.application)
        )[DiaryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val allDiaryAdapter = AllDiaryAdapter(onItemClick, onDeleteClick)

        rv_diary_search.apply {
            adapter = allDiaryAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        edt_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    var keyword =
                        "%${s.toString().toLowerCase()}%"
                    diaryViewModel.searchDiary(keyword).observe(this@SearchActivity, Observer {
                        it.sortWith(
                            compareBy<Diary>(
                                { it.year },
                                { it.month },
                                { it.date },
                                { it.time }).reversed()
                        )
                        allDiaryAdapter.setListDiary(it)
                    })
                } else allDiaryAdapter.clearListDiary()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        btn_back.setOnClickListener { onBackPressed() }
    }

    private val onItemClick: (diary: Diary) -> Unit = {
        var intent = Intent(this, DetailDiaryActivity::class.java)
        intent.putExtra("Detail Diary", it)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private val onDeleteClick: (diary: Diary) -> Unit = {
        diaryViewModel.delete(it)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}