package com.example.mydiary.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.mydiary.database.DiaryDatabase
import com.example.mydiary.database.dao.DiaryDao
import com.example.mydiary.model.Diary

class DiaryRepository(app: Application) {
    private val diaryDao: DiaryDao

    init {
        val diaryDatabase: DiaryDatabase = DiaryDatabase.getInstance(app)
        diaryDao = diaryDatabase.getDiaryDao()
    }

    suspend fun insertDiary(diary: Diary) = diaryDao.insertDiary(diary)

    suspend fun updateDiary(diary: Diary) = diaryDao.updateDiary(diary)

    fun getDiaryDateByMonth(year:Int, month:Int): LiveData<List<Int>> = diaryDao.getDiaryDateByMonth(year, month)

    fun getDiaryOfDate(year: Int, month: Int, date:Int): LiveData<MutableList<Diary>> = diaryDao.getDiaryOfDate(year, month, date)

    fun getAllDiary():LiveData<MutableList<Diary>> = diaryDao.getAllDiary()

    fun getDetailDiary(year: Int, month: Int, date: Int, time:String):LiveData<Diary> = diaryDao.getDetailDiary(year, month, date, time)

}