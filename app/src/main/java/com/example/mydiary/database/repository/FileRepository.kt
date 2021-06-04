package com.example.mydiary.database.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.mydiary.model.Diary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder

class FileRepository(val context: Context) {

    suspend fun writeDataToFile(list: MutableList<Diary>) {
        withContext(Dispatchers.IO) {
            val data = StringBuilder()
            data.append("year_col" + "," + "month_col" + "," + "date_col" + "," + "time_col" + "," + "title_col" + "," + "content_col")
            for (i in list) {
                data.append("\n" + i.year + "," + i.month + "," + i.date + "," + i.time + "," + i.title + "," + i.content)
            }
            try {
                val fileout: FileOutputStream =
                    context.openFileOutput("data.csv", Context.MODE_PRIVATE)
                fileout.write(data.toString().toByteArray())
                fileout.close()
            } catch (e: Exception) {
                Log.d("aaaa", "writeDataToFileException: $e")
            }
        }
    }

    suspend fun readDataFromFile(uri: Uri): List<String> {
        return withContext(Dispatchers.IO) {
            var line: List<String> = listOf()
            try {
                val csvFile = context.contentResolver.openInputStream(uri)
                val isr = InputStreamReader(csvFile)
                line = BufferedReader(isr).readLines()
                isr.close()
                csvFile!!.close()
            } catch (e: Exception) {
                Log.d("aaaa", "readDataToFileException: $e")
            }
            return@withContext line
        }
    }

    suspend fun readDataFromFileInternal(): List<String> {
        return withContext(Dispatchers.IO) {
            val fileInput: FileInputStream = context.openFileInput("data.csv")
            var line: List<String> = BufferedReader(InputStreamReader(fileInput)).readLines()
            fileInput.close()
            fileInput.close()
            return@withContext line
        }
    }

}