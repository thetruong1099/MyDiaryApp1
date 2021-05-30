package com.example.mydiary.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.mydiary.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        var data = intent
        var password = data.getStringExtra("password")
        edt_password_login.setText(password)

        var sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE)
        var passwordSetted = sharedPreferences.getString("myPassword","")

        btn_login.setOnClickListener {
            var curentPassword = edt_password_login.text.toString()

            if (curentPassword != passwordSetted){
                showDialogCheckPass("Password nhiều hơn 6 ký tự")
            }else{
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }
        }

        btn_forgot_password.setOnClickListener {
            var intent = Intent(this, ResetPassActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun showDialogCheckPass(string: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(string)
            .setPositiveButton("Yes") { dialogInterface, which -> }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}