package com.example.mobile_app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mobile_app.data.MoodEntry
import com.example.mobile_app.repository.MoodRepository


class MoodViewModel : ViewModel() {
    private val repository = MoodRepository()

    fun saveMood(entry: MoodEntry) {
        repository.saveMood(entry)
    }
}
