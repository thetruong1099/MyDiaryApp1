package com.example.mydiary.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.mydiary.R
import com.example.mydiary.viewmodel.SharedPreferenceViewModel
import kotlinx.android.synthetic.main.activity_reset_pass.*
import kotlinx.coroutines.*

class ResetPassActivity : AppCompatActivity() {

    private lateinit var job: Job

    private val sharePreferenceViewModel by lazy {
        ViewModelProvider(
            this,
            SharedPreferenceViewModel.SharePreferenceViewModelFactory(this)
        )[SharedPreferenceViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pass)

        job = Job()

        btn_reset_password.setOnClickListener {
            counterTime()
        }

    }

    private fun counterTime() {
        job = CoroutineScope(Dispatchers.Default).launch {
            var numTime = 11
            repeat(11) {
                numTime--
                delay(1000)
                updateUi(numTime)
            }
            if (numTime == 0) intentToConfirmPassword()
        }
    }

    private fun intentToConfirmPassword() {
        sharePreferenceViewModel.setPassWord("123456")
        var intent = Intent(this, ConfirmResetPassActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private suspend fun updateUi(number: Int) {
        withContext(Dispatchers.Main) {
            tv_time_reset_pass.text = "Password will reset after $number seconds"
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onStop() {
        super.onStop()
        job.cancel()

    }
}