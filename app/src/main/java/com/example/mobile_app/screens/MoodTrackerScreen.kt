package com.example.mobile_app.screens
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.mobile_app.data.MoodEntry
import com.example.mobile_app.data.MoodTrackerData
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.draw.shadow
import com.example.mobile_app.R
import java.time.LocalDate



@Composable
fun MoodTrackerScreen() {
    var selectedMood by remember { mutableStateOf("") }
    val journalText by remember { mutableStateOf(TextFieldValue("")) }
    var moodScore by remember { mutableIntStateOf(5) }
    var isMusicPlaying by remember { mutableStateOf(false) }

    val moods = listOf("Happy", "Sad", "Angry", "Excited", "Neutral")
    val moodIcons = listOf("🙂", "😢", "😡", "😆", "😐")
    val moodColors = listOf(
        Color(0xFF81C784), // Light Green for Happy
        Color(0xFF388E3C), // Dark Green for Sad
        Color(0xFFF44336), // Red for Angry
        Color(0xFF64B5F6), // Light Blue for Excited
        Color(0xFF9E9E9E)  // Grey for Neutral
    )

    val musicResources = mapOf(
        "Happy" to R.raw.happy_music,
        "Sad" to R.raw.sad_music,
        "Angry" to R.raw.angry_music,
        "Excited" to R.raw.excited_music,
        "Neutral" to R.raw.neutral_music
    )

    val context = LocalContext.current

    // MediaPlayer to handle music playback
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }


    fun stopMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.release()
            }
        }
    }


    fun playMoodMusic(mood: String) {
        stopMusic()
        val musicResId = musicResources[mood]
        musicResId?.let {
            mediaPlayer = MediaPlayer.create(context, it)
            mediaPlayer?.start()
        }
    }

    // Function to toggle music play/pause
    fun toggleMusic() {
        if (isMusicPlaying) {
            mediaPlayer?.pause()
        } else {
            mediaPlayer?.start()
        }
        isMusicPlaying = !isMusicPlaying
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .background(Color(0xFFE8F5E9)) // Soft Green Background
    ) {
        // Title Section
        Text(
            text = "Mood Tracker",
            style = MaterialTheme.typography.headlineMedium.copy(color = Color(0xFF388E3C)),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Mood Selection Section
        Text("How do you feel today?", style = MaterialTheme.typography.bodyLarge)

        // Mood selection with interactive buttons
        // Mood selection with expressive cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            moods.forEachIndexed { index, mood ->
                val isSelected = selectedMood == mood
                val animatedElevation by animateDpAsState(targetValue = if (isSelected) 10.dp else 2.dp)
                val animatedColor by animateColorAsState(
                    targetValue = if (isSelected) moodColors[index] else moodColors[index].copy(alpha = 0.4f)
                )

                Card(
                    modifier = Modifier
                        .size(80.dp)
                        .shadow(animatedElevation, shape = CircleShape)
                        .clickable {
                            selectedMood = mood
                            playMoodMusic(mood)
                        },
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = animatedColor),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = moodIcons[index],
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = mood,
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                        )
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Mood Score Section
        Text("Select your mood intensity:", style = MaterialTheme.typography.bodyLarge)

        // Mood score slider with dynamic color change
        Slider(
            value = moodScore.toFloat(),
            onValueChange = { moodScore = it.toInt() },
            valueRange = 1f..10f,
            steps = 8,
            colors = SliderDefaults.colors(
                thumbColor = when (moodScore) {
                    in 1..3 -> Color(0xFFF44336)
                    in 4..7 -> Color(0xFF388E3C)
                    else -> Color(0xFF81C784)
                },
                activeTrackColor = when (moodScore) {
                    in 1..3 -> Color(0xFFF44336)
                    in 4..7 -> Color(0xFF388E3C)
                    else -> Color(0xFF81C784)
                },
                inactiveTrackColor = Color(0xFFBDBDBD)
            ),
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        )

        Text("Mood score: $moodScore/10", style = MaterialTheme.typography.bodyMedium)

        // Save Mood Button
        Spacer(modifier = Modifier.height(24.dp))
        ElevatedButton(
            onClick = {
                // Save the mood and journal entry to the data
                MoodTrackerData.addMoodEntry(MoodEntry(
                    date = LocalDate.now(),
                    mood = selectedMood,
                    note = journalText.text
                ))
                Toast.makeText(context, "Mood saved!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Save Mood", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))


        MoodHistorySection()

        Spacer(modifier = Modifier.height(24.dp))


        IconButton(
            onClick = { toggleMusic() },
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 16.dp)
        ) {
            Icon(
                imageVector = if (isMusicPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isMusicPlaying) "Pause Music" else "Play Music",
                tint = Color(0xFF388E3C)
            )
        }
    }
}

@Composable
fun MoodHistorySection() {
    val moodEntries = MoodTrackerData.getMoodData()

    if (moodEntries.isEmpty()) {
        Text("No mood history yet. Start tracking your mood!", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            moodEntries.forEach { entry ->
                MoodHistoryCard(entry = entry)
            }
        }
    }
}

// Mood History Card with Green Themes
@Composable
fun MoodHistoryCard(entry: MoodEntry) {
    val moodColor = when (entry.mood) {
        "Happy" -> Color(0xFF81C784)
        "Excited" -> Color(0xFF64B5F6)
        "Neutral" -> Color(0xFF9E9E9E)
        "Sad" -> Color(0xFF388E3C)
        "Angry" -> Color(0xFFF44336)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = moodColor),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${entry.mood} - ${entry.date}",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = entry.note.take(20) + "...", // Show a preview of the note
                style = MaterialTheme.typography.bodySmall.copy(color = Color.White),
                modifier = Modifier.weight(2f)
            )
        }
    }
}
