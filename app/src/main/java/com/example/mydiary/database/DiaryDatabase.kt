package com.example.mydiary.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mydiary.database.dao.DiaryDao
import com.example.mydiary.model.Diary

@SuppressLint("RestrictedApi")
@Database(
    entities = [Diary::class],
    version = 1,
)
abstract class DiaryDatabase : RoomDatabase() {

    abstract fun getDiaryDao(): DiaryDao

    companion object {
        @Volatile
        private var instance: DiaryDatabase? = null

        fun getInstance(context: Context): DiaryDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context, DiaryDatabase::class.java, "DiaryDataBase")
                        .build()
            }
            return instance!!
        }
    }
}