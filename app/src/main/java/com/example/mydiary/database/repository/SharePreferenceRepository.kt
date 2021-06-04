package com.example.mydiary.database.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SharePreferenceRepository(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("myData", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    suspend fun setPassword(password:String){
        withContext(Dispatchers.IO){
            editor.putString("myPassword", password)
            editor.commit()
        }
    }

    suspend fun getPassword():String?{
        return withContext(Dispatchers.IO){
            return@withContext sharedPreferences.getString("myPassword","")
        }
    }

    fun setDayStartOfWeek(dayStart:String){
        editor.putString("dayStart", dayStart)
        editor.commit()
    }

    fun getDayStart() = SharePrefeerenceLiveData(sharedPreferences,"dayStart")
}