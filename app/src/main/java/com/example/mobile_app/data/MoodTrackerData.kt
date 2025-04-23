package com.example.mobile_app.data

import androidx.compose.runtime.mutableStateListOf
import java.time.LocalDate

object MoodTrackerData {
    private val moodEntries = mutableStateListOf<MoodEntry>()

    fun addMoodEntry(entry: MoodEntry) {
        moodEntries.add(entry)
    }

    fun getMoodData(): List<MoodEntry> = moodEntries
}
