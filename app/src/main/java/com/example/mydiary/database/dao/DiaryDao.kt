package com.example.mydiary.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mydiary.model.Diary

@Dao
interface DiaryDao {
    @Insert
    suspend fun insertDiary(diary: Diary)

    @Update
    suspend fun updateDiary(diary: Diary)

    @Query("select date_col from diary_table where year_col = :year and month_col =:month")
    fun getDiaryDateByMonth(year: Int, month: Int): LiveData<List<Int>>

    @Query("select * from diary_table where year_col = :year and month_col =:month and date_col =:date")
    fun getDiaryOfDate(year: Int, month: Int, date: Int): LiveData<MutableList<Diary>>

    @Query("select * from diary_table")
    fun getAllDiary(): LiveData<MutableList<Diary>>

    @Query("select * from diary_table where year_col = :year and month_col =:month and date_col =:date and time_col =:time")
    fun getDetailDiary(year: Int, month: Int, date: Int, time:String):LiveData<Diary>

}