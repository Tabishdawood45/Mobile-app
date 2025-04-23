package com.example.mobile_app.data



import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "note_prefs")

class NoteDataStore(private val context: Context) {

    suspend fun saveNote(dateKey: String, note: String) {
        context.dataStore.edit { prefs ->
            prefs[stringPreferencesKey("note_$dateKey")] = note
        }
    }

    suspend fun saveReminderTime(dateKey: String, hour: Int, minute: Int) {
        context.dataStore.edit { prefs ->
            prefs[intPreferencesKey("hour_$dateKey")] = hour
            prefs[intPreferencesKey("minute_$dateKey")] = minute
        }
    }

    suspend fun deleteNote(dateKey: String) {
        context.dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey("note_$dateKey"))
        }
    }

    suspend fun deleteReminderTime(dateKey: String) {
        context.dataStore.edit { preferences ->
            preferences.remove(intPreferencesKey("hour_$dateKey"))
            preferences.remove(intPreferencesKey("minute_$dateKey"))
        }
    }

    fun getAllNotes(): Flow<Map<String, String>> {
        return context.dataStore.data.map { prefs ->
            prefs.asMap().mapNotNull { (key, value) ->
                if (key.name.startsWith("note_") && value is String) {
                    key.name.removePrefix("note_") to value
                } else null
            }.toMap()
        }
    }

    fun getAllReminderTimes(): Flow<Map<String, Pair<Int, Int>>> {
        return context.dataStore.data.map { prefs ->
            val result = mutableMapOf<String, Pair<Int, Int>>()
            val grouped = prefs.asMap().filterKeys { it.name.startsWith("hour_") || it.name.startsWith("minute_") }
            val groupedByDate = grouped.entries.groupBy { it.key.name.substringAfter('_') }

            groupedByDate.forEach { (date, entries) ->
                val hour = entries.find { it.key.name.startsWith("hour_") }?.value as? Int
                val minute = entries.find { it.key.name.startsWith("minute_") }?.value as? Int
                if (hour != null && minute != null) result[date] = hour to minute
            }

            result
        }
    }
}
