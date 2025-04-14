package com.example.mobile_app.data
import java.time.LocalDate

data class MoodEntry(
    val date: LocalDate,
    val mood: String,
    val note: String
)
