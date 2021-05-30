package com.example.mydiary.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "diary_table")
data class Diary(
    @ColumnInfo(name = "year_col") var year: Int = 0,
    @ColumnInfo(name = "month_col") var month: Int = 0,
    @ColumnInfo(name = "date_col") var date: Int = 0,
    @ColumnInfo(name = "time_col") var time: String = "",
    @ColumnInfo(name = "title_col") var title: String = "",
    @ColumnInfo(name = "content_col") var content: String = ""
): Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_col")
    var id: Int = 0
}