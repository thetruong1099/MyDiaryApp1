package com.example.mydiary.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mydiary.database.repository.FileRepository
import com.example.mydiary.model.Diary
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException

class FileViewModel(context: Context) : ViewModel() {
    private val fileRepository: FileRepository = FileRepository(context)

    fun writeDataToFile(list: MutableList<Diary>) =
        CoroutineScope(Dispatchers.IO).launch { fileRepository.writeDataToFile(list) }

    fun readDataFromFile(uri: Uri):List<String> = runBlocking {
        fileRepository.readDataFromFile(uri)
    }

    fun readDataFromFileInternal():List<String> = runBlocking {
        fileRepository.readDataFromFileInternal()
    }

    class FileViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FileViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FileViewModel(context) as T
            }
            throw IllegalArgumentException("unable construct viewModel")
        }
    }
}