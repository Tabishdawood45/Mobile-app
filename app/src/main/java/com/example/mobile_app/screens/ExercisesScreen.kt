package com.example.mobile_app.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle

import androidx.compose.ui.draw.shadow

@Composable
fun ExercisesScreen() {
    var selectedExercise by remember { mutableStateOf("") }
    val completedExercises = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            "Mental Health Exercises",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (selectedExercise.isEmpty()) {
            ExerciseCard("🧘 Breathing Exercise", completedExercises.contains("breathing")) {
                selectedExercise = "breathing"
            }
            ExerciseCard("✍️ Grounding Exercise", completedExercises.contains("grounding")) {
                selectedExercise = "grounding"
            }
            ExerciseCard("🌬️ Muscle Relaxation", completedExercises.contains("relaxation")) {
                selectedExercise = "relaxation"
            }
        } else {
            when (selectedExercise) {
                "breathing" -> BreathingExercise()
                "grounding" -> GroundingExercise()
                "relaxation" -> MuscleRelaxationGuide()
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (!completedExercises.contains(selectedExercise)) {
                        completedExercises.add(selectedExercise)
                    }
                    selectedExercise = ""
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("✅ Done! Back to Menu", color = Color.White)
            }
        }
    }
}

@Composable
fun ExerciseCard(title: String, isCompleted: Boolean = false, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.weight(1f)
            )
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Completed",
                    tint = Color.Green,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun BreathingExercise() {
    var isInhaling by remember { mutableStateOf(true) }
    val scale by animateFloatAsState(
        targetValue = if (isInhaling) 1.5f else 0.5f,
        animationSpec = tween(durationMillis = 4000),
        label = "BreathingAnimation"
    )

    LaunchedEffect(Unit) {
        while (true) {
            isInhaling = !isInhaling
            delay(4000)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = if (isInhaling) "Inhale..." else "Exhale...",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .size(180.dp)
                .graphicsLayer { scaleX = scale; scaleY = scale }
                .background(Color.Cyan.copy(alpha = 0.5f), CircleShape)
                .shadow(15.dp, CircleShape)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Focus on your breathing and stay calm.",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroundingExercise() {
    val prompts = listOf(
        "5 things you can see:",
        "4 things you can touch:",
        "3 things you can hear:",
        "2 things you can smell:",
        "1 thing you can taste:"
    )
    val responses = remember { mutableStateListOf("", "", "", "", "") }

    Column(modifier = Modifier.fillMaxWidth()) {
        prompts.forEachIndexed { index, prompt ->
            Text(
                text = prompt,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
            )
            OutlinedTextField(
                value = responses[index],
                onValueChange = { responses[index] = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
fun MuscleRelaxationGuide() {
    val steps = listOf(
        "Tense your shoulders... hold... now relax.",
        "Clench your fists... hold... now relax.",
        "Tighten your stomach... hold... now relax.",
        "Press your legs together... hold... now relax.",
        "Scrunch your face... hold... now relax."
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        steps.forEach {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Text(
                    text = it,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                )
            }
        }
    }
}
