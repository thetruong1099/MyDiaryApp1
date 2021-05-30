package com.example.mydiary.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mydiary.R
import kotlinx.android.synthetic.main.activity_reset_pass.*
import kotlinx.coroutines.*

class ResetPassActivity : AppCompatActivity() {

    private lateinit var job: Job

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
            var numTime = 31
            repeat(31) {
                numTime--
                delay(1000)
                updateUi(numTime)
            }
            if (numTime == 0) intentToConfirmPassword()
        }
    }

    private fun intentToConfirmPassword() {
        resetPassword()
        var intent = Intent(this, ConfirmResetPassActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun resetPassword() {
        val sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("myPassword", "1234567")
        editor.commit()
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