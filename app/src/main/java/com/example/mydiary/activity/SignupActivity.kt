package com.example.mydiary.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mydiary.R
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        var sharedPreferences:SharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()

        btn_signup.setOnClickListener {
            var password = edt_password_signup.text.toString()

            if (password.length<=6){
                showDialogCheckPass("Password nhiều hơn 6 ký tự")
            }else{
                //save SharedPreferences
                editor.putString("myPassword", password)
                editor.commit()

                //start login activity
                var intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("password", password)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }
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