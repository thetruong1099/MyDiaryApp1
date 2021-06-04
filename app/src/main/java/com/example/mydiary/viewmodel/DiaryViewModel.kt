package com.example.mydiary.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mydiary.database.repository.DiaryRepository
import com.example.mydiary.model.Diary
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class DiaryViewModel(application: Application) : ViewModel() {
    private val diaryRepository: DiaryRepository = DiaryRepository(application)

    fun insertDiary(diary: Diary) = viewModelScope.launch {
        diaryRepository.insertDiary(diary)
    }

    fun updateDiary(diary: Diary) = viewModelScope.launch {
        diaryRepository.updateDiary(diary)
    }

    fun delete(diary: Diary) = viewModelScope.launch {
        diaryRepository.delete(diary)
    }

    fun deleteAllDiary() = viewModelScope.launch { diaryRepository.deleteAllDiary() }

    fun getDiaryDateByMonth(year: Int, month: Int): LiveData<List<Int>> =
        diaryRepository.getDiaryDateByMonth(year, month)

    fun getDiaryOfDate(year: Int, month: Int, date: Int): LiveData<MutableList<Diary>> =
        diaryRepository.getDiaryOfDate(year, month, date)

    fun getAllDiary(): LiveData<MutableList<Diary>> = diaryRepository.getAllDiary()

    fun getDetailDiary(year: Int, month: Int, date: Int, time: String): LiveData<Diary> =
        diaryRepository.getDetailDiary(year, month, date, time)

    fun searchDiary(keyword: String): LiveData<MutableList<Diary>> =
        diaryRepository.searchDiary(keyword)

    class DiaryViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DiaryViewModel(application) as T
            }

            throw IllegalArgumentException("unable construct viewModel")
        }

    }
}