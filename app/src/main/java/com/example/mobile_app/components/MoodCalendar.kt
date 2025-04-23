package com.example.mobile_app.components

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile_app.data.NoteDataStore
import com.example.mobile_app.notifications.ReminderReceiver
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun MoodCalendar(onMoodSelected: (LocalDate, String, String) -> Unit) {
    val context = LocalContext.current
    val today = remember { LocalDate.now() }
    var selectedDate by remember { mutableStateOf(today) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showNoteDialog by remember { mutableStateOf(false) }
    var reminderHour by remember { mutableIntStateOf(12) }
    var reminderMinute by remember { mutableIntStateOf(0) }
    val storedNotes = remember { mutableStateMapOf<LocalDate, MutableList<String>>() }
    val storedReminders = remember { mutableStateMapOf<LocalDate, MutableList<Pair<Int, Int>>>() }
    var noteText by remember { mutableStateOf(TextFieldValue("")) }
    val noteStore = remember { NoteDataStore(context) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        noteStore.getAllNotes().collect { notes ->
            storedNotes.clear()
            notes.forEach { (key, value) ->
                storedNotes[LocalDate.parse(key)] = mutableListOf(value)
            }
        }
    }

    LaunchedEffect(Unit) {
        noteStore.getAllReminderTimes().collect { reminders ->
            storedReminders.clear()
            reminders.forEach { (dateStr, time) ->
                val date = LocalDate.parse(dateStr)
                storedReminders[date] = mutableListOf(time)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = Color(0xFFF1F8E9)), // subtle green background
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Notes & Reminder",
            style = MaterialTheme.typography.headlineMedium.copy(color = Color(0xFF2E7D32))
        )

        // Calendar View
        CalendarView(
            selectedDate = selectedDate,
            storedNotes = storedNotes,
            onDateSelected = { date -> selectedDate = date }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons to set reminders and add notes
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { showTimePicker = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Set Reminder")
            }

            Button(
                onClick = { showNoteDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Add Note")
            }
        }

        // Spacer between buttons and content
        Spacer(modifier = Modifier.height(16.dp))

        // Scrollable content for notes and reminders
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {

            // Display Notes if available
            if (storedNotes.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(
                        "📒 Your Notes:",
                        style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF2E7D32)),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    storedNotes.toSortedMap().forEach { (date, notes) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFA5D6A7)),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "🗓️ ${date.format(DateTimeFormatter.ofPattern("MMM dd"))}",
                                    style = MaterialTheme.typography.labelLarge
                                )
                                notes.forEach { note ->
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(note, style = MaterialTheme.typography.bodyMedium)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = {
                                            storedNotes[date]?.clear() // Clear all notes for that date
                                            coroutineScope.launch {
                                                noteStore.deleteNote(date.toString())
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
                                    ) {
                                        Text("Delete", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display Reminders if available
            if (storedReminders.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(
                        "⏰ Your Reminders:",
                        style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF2E7D32)),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    storedReminders.toSortedMap().forEach { (date, reminders) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFDCEDC8)),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                reminders.forEach { timePair ->
                                    val timeStr = String.format("%02d:%02d", timePair.first, timePair.second)
                                    Text(
                                        text = "🗓️ ${date.format(DateTimeFormatter.ofPattern("MMM dd"))} at ⏰ $timeStr",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = {
                                            storedReminders.remove(date)
                                            coroutineScope.launch {
                                                noteStore.deleteReminderTime(date.toString())
                                            }
                                            cancelReminder(context, date)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
                                    ) {
                                        Text("Delete", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Time Picker
    if (showTimePicker) {
        android.app.TimePickerDialog(context, { _, hour, minute ->
            reminderHour = hour
            reminderMinute = minute
            setCustomReminder(context, selectedDate, "Reminder!!", hour, minute)
            coroutineScope.launch {
                noteStore.saveReminderTime(selectedDate.toString(), hour, minute)
            }
            showTimePicker = false
        }, reminderHour, reminderMinute, false).show()
    }

    // Note Dialog
    if (showNoteDialog) {
        AlertDialog(
            onDismissRequest = { showNoteDialog = false },
            title = { Text("Add Note for ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd"))}") },
            text = {
                Column {
                    TextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        placeholder = { Text("Enter your note...") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    // Add new note to the list
                    storedNotes[selectedDate]?.add(noteText.text) ?: storedNotes.put(selectedDate, mutableListOf(noteText.text))
                    coroutineScope.launch {
                        noteStore.saveNote(selectedDate.toString(), noteText.text)
                    }
                    setNoteReminder(context, selectedDate, noteText.text)
                    showNoteDialog = false
                }) { Text("Save") }
            },
            dismissButton = {
                Button(onClick = { showNoteDialog = false }) { Text("Cancel") }
            }
        )
    }
}




@Composable
fun CalendarView(selectedDate: LocalDate, storedNotes: SnapshotStateMap<LocalDate, MutableList<String>>, onDateSelected: (LocalDate) -> Unit) {

    val currentMonth = remember { YearMonth.now() }
    val firstDayOfMonth = currentMonth.atDay(1)
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value + 6) % 7 // 0 = Sunday, 1 = Monday, etc.
    val today = LocalDate.now()

    val weekDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Column {
        // Weekday Headers
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            weekDays.forEach { day ->
                Text(text = day, style = MaterialTheme.typography.labelLarge)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Calendar Days
        LazyVerticalGrid(columns = GridCells.Fixed(7), userScrollEnabled = false) {
            items(firstDayOfWeek) {
                Box(modifier = Modifier.size(48.dp))
            }

            // Actual days
            items(daysInMonth) { index ->
                val dayOfMonth = index + 1
                val date = firstDayOfMonth.withDayOfMonth(dayOfMonth)
                val isToday = date == today
                val isSelected = date == selectedDate

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(48.dp)
                        .background(
                            color = when {
                                isToday -> Color(0xFFB2FF59) // lime green for today
                                isSelected -> Color(0xFF81C784) // darker green for selection
                                storedNotes.containsKey(date) -> Color(0xFFFFFF8D) // yellow for notes
                                else -> Color(0xFFE8F5E9) // lightest green for others
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onDateSelected(date) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = dayOfMonth.toString(), color = Color.Black)
                }
            }
        }
    }
}

@SuppressLint("ScheduleExactAlarm")
fun setNoteReminder(context: Context, date: LocalDate, note: String) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra("NOTE", note)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        date.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val calendar = Calendar.getInstance().apply {
        set(
            date.year,
            date.monthValue - 1,
            date.dayOfMonth,
            9, 0, 0 // Set the default reminder time to 9:00 AM
        )
    }

    // Check if the selected reminder time is in the past
    if (calendar.timeInMillis > System.currentTimeMillis()) {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Note saved & reminder set at 9:00 AM!", Toast.LENGTH_SHORT).show()
    } else {
        // If it's in the past, set for the next day at 9 AM
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Reminder time is in the past. Set for next day.", Toast.LENGTH_SHORT).show()
    }
}

@SuppressLint("ScheduleExactAlarm")
fun setCustomReminder(context: Context, date: LocalDate, note: String, hour: Int, minute: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra("NOTE", note)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        date.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val calendar = Calendar.getInstance().apply {
        set(
            date.year,
            date.monthValue - 1,
            date.dayOfMonth,
            hour, minute, 0
        )
    }

    // Check if the selected reminder time is in the past
    if (calendar.timeInMillis > System.currentTimeMillis()) {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Reminder set at $hour:$minute!", Toast.LENGTH_SHORT).show()
    } else {
        // If it's in the past, set for the next possible day at the same time
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Reminder time is in the past. Set for next day.", Toast.LENGTH_SHORT).show()
    }
}

fun cancelReminder(context: Context, date: LocalDate) {
    val intent = Intent(context, ReminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        date.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
    Toast.makeText(context, "Reminder for ${date.format(DateTimeFormatter.ofPattern("MMM dd"))} cancelled!", Toast.LENGTH_SHORT).show()
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MoodCalendarPreview() {
    MaterialTheme {
        MoodCalendar { _, _, _ -> }
    }
}