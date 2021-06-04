package com.example.mydiary.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.mydiary.R
import com.example.mydiary.viewmodel.SharedPreferenceViewModel
import kotlinx.coroutines.*

class StartActivity : AppCompatActivity() {

    private lateinit var job1: Job
    private lateinit var job2: Job

    private val sharePreferenceViewModel by lazy {
        ViewModelProvider(
            this,
            SharedPreferenceViewModel.SharePreferenceViewModelFactory(this)
        )[SharedPreferenceViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        job1 = Job()
        job2 = Job()

        val password = sharePreferenceViewModel.getPassword()
        if (password == "") setToSignupActivity()
        else setToLoginActivity()
    }

    private fun setToLoginActivity() {
        var intent = Intent(this, LoginActivity::class.java)
        job1 = CoroutineScope(Dispatchers.Main).launch {
            delay(3500)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
    }

    private fun setToSignupActivity() {
        var intent = Intent(this, SignupActivity::class.java)
        job2 = CoroutineScope(Dispatchers.Main).launch {
            delay(3500)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        job1.cancel()
        job2.cancel()
    }
}