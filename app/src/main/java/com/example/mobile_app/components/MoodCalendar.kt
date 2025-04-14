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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.mobile_app.notifications.ReminderReceiver
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar



@Composable
fun MoodCalendar(onMoodSelected: (LocalDate, String, String) -> Unit) {
    val context = LocalContext.current
    val today = remember { LocalDate.now() }
    var selectedDate by remember { mutableStateOf(today) }
    var selectedMood by remember { mutableStateOf<String?>(null) }
    var showMoodDialog by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showNoteDialog by remember { mutableStateOf(false) }
    var reminderHour by remember { mutableIntStateOf(12) }
    var reminderMinute by remember { mutableIntStateOf(0) }
    val storedMoods = remember { mutableStateMapOf<LocalDate, String>() }
    val storedNotes = remember { mutableStateMapOf<LocalDate, String>() }
    var noteText by remember { mutableStateOf(TextFieldValue("")) }

    val moodMessages = mapOf(
        "happy" to "Stay happy and positive!",
        "sad" to "It's okay to feel down. Try to get happy!",
        "neutral" to "You're doing great. Keep going!"
    )

    val selectedQuote = moodMessages[selectedMood] ?: "Choose your mood"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mood & Reminder", style = MaterialTheme.typography.headlineMedium)

        // Calendar always visible
        CalendarView(
            selectedDate = selectedDate,
            storedNotes = storedNotes,
            onDateSelected = { date ->
                selectedDate = date
            }
        )

        // Reminder Button
        Button(onClick = { showTimePicker = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Set Reminder")
        }

        // Mood Selection Button
        Button(onClick = { showMoodDialog = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Select Mood")
        }

        // Add Note Button
        Button(onClick = { showNoteDialog = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Add a Note")
        }

        // Display today's Mood Quote
        selectedMood?.let {
            Text(
                text = selectedQuote,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        // Show Note for selectedDate
        val scrollState = rememberScrollState()

        if (storedNotes.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(top = 8.dp)
            ) {
                Text("Your Notes:", style = MaterialTheme.typography.titleMedium)

                // Show all notes sorted by date
                storedNotes.toSortedMap().forEach { (date, note) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Note for ${date.format(DateTimeFormatter.ofPattern("MMM dd"))}",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(note, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
        // Mood Dialog
    if (showMoodDialog) {
        AlertDialog(
            onDismissRequest = { showMoodDialog = false },
            title = { Text("Select Your Mood") },
            text = {
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    listOf("😀", "😐", "😞").forEach { mood ->
                        Text(
                            text = mood,
                            modifier = Modifier.clickable {
                                selectedMood = when (mood) {
                                    "😀" -> "happy"
                                    "😐" -> "neutral"
                                    "😞" -> "sad"
                                    else -> "neutral"
                                }
                                storedMoods[selectedDate] = mood
                                showMoodDialog = false
                                onMoodSelected(selectedDate, mood, storedNotes[selectedDate] ?: "")
                            },
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            },
            confirmButton = {}
        )
    }

    // Time Picker
    if (showTimePicker) {
        android.app.TimePickerDialog(context, { _, hour, minute ->
            reminderHour = hour
            reminderMinute = minute
            setCustomReminder(context, selectedDate, "Reminder!!", hour, minute)
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
                    storedNotes[selectedDate] = noteText.text
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
fun CalendarView(selectedDate: LocalDate, storedNotes: Map<LocalDate, String>, onDateSelected: (LocalDate) -> Unit) {

    val currentMonth = remember { YearMonth.now() }
    val firstDayOfMonth = currentMonth.atDay(1)
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value + 6) % 7// 0 = Sunday, 1 = Monday, etc.
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
                            when {
                                isToday -> Color(0xFFB3E5FC) // light blue for today
                                isSelected -> Color(0xFFC8E6C9) // light green for selected
                                storedNotes.containsKey(date) -> Color.Yellow
                                else -> Color.LightGray
                            },
                            shape = RoundedCornerShape(8.dp)
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
            hour, minute, 0 // Set the reminder time to the user-selected hour and minute
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



