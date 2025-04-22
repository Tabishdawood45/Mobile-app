package com.example.mobile_app.repository


import com.example.mobile_app.data.MoodEntry
import java.time.LocalDate

class MoodRepository {
    private val moodData = mutableMapOf<LocalDate, MoodEntry>()

    fun saveMood(entry: MoodEntry) {
        moodData[entry.date] = entry
    }

}