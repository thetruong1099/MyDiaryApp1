package com.example.mydiary.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.example.mydiary.R
import com.example.mydiary.model.Diary
import com.example.mydiary.util.FormatTime
import com.example.mydiary.viewmodel.DiaryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_write_diary.*
import kotlinx.android.synthetic.main.layout_bottom_sheet.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

class WriteDiaryActivity : AppCompatActivity() {

    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(
            this,
            DiaryViewModel.DiaryViewModelFactory(this.application)
        )[DiaryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_diary)

        //form month Fragment
        var year = intent.getIntExtra("year", 0)
        var month = intent.getIntExtra("month", 0)
        var date = intent.getIntExtra("day", 0)


        //form detail diary activity

        var check = intent.getBooleanExtra("check", false)

        var diaryEdit = Diary()
        if (check) {
            diaryEdit = intent.getSerializableExtra("Diary") as Diary
            year = diaryEdit.year
            month = diaryEdit.month
            date = diaryEdit.date

            btn_edit_date.visibility = View.GONE
            edt_title.setText(diaryEdit.title)
            edt_story.setText(diaryEdit.content)
        }

        //from diary fragment
        val checkFromDiaryFragment = intent.getBooleanExtra("checkFromDiaryFragment", false)
        if (checkFromDiaryFragment) {
            var calender = Calendar.getInstance()
            year = calender.get(Calendar.YEAR)
            month = calender.get(Calendar.MONTH + 1)
            date = calender.get(Calendar.DATE)
        }

        tv_date_time.text = FormatTime.formatDateTime(year, month, date, "EEEE, dd MMMM YYYY")

        val theme = AlertDialog.THEME_HOLO_LIGHT

        btn_edit_date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                theme,
                DatePickerDialog.OnDateSetListener { view, choseYear, monthOfYear, dayOfMonth ->
                    year = choseYear
                    month = monthOfYear + 1
                    date = dayOfMonth

                    tv_date_time.text = FormatTime.formatDateTime(
                        year,
                        monthOfYear + 1,
                        dayOfMonth,
                        "EEEE, dd MMMM YYYY"
                    )
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePickerDialog.show()
        }

        btn_save.setOnClickListener {
            val time = FormatTime.formatDateTime("kk:mm:ss")
            val title: String = edt_title.text.toString()
            val content: String = edt_story.text.toString()

            if (title == "" || content == "") showDialog("Không để chóng các trường")
            else {
                if (check) {
                    diaryEdit.title = title
                    diaryEdit.content = content
                    diaryViewModel.updateDiary(diaryEdit)
                } else {
                    val diary = Diary(year, month, date, time, title, content)
                    diaryViewModel.insertDiary(diary)
                }
                finish()
            }
        }

        btn_exit.setOnClickListener {
            onBackPressed()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun showDialog(string: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(string)
            .setPositiveButton("Yes") { dialogInterface, which -> }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}