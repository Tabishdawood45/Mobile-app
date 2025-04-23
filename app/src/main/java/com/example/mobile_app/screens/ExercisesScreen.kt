package com.example.mobile_app.screens
import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.ui.text.font.FontWeight
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ExercisesScreen() {
    var selectedExercise by remember { mutableStateOf("") }
    val completedExercises = remember { mutableStateListOf<String>() }

    val softGreen = Color(0xFFE8F5E9)
    val deepGreen = Color(0xFF2E7D32)
    val cardGreen = Color(0xFFA5D6A7)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(softGreen)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Text(
            "Mental Health Exercises",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = deepGreen,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (selectedExercise.isEmpty()) {
            ExerciseCard("🧘 Breathing Exercise", completedExercises.contains("breathing"), cardGreen) {
                selectedExercise = "breathing"
            }
            ExerciseCard("✍️ Grounding Exercise", completedExercises.contains("grounding"), cardGreen) {
                selectedExercise = "grounding"
            }
            ExerciseCard("🌬️ Muscle Relaxation", completedExercises.contains("relaxation"), cardGreen) {
                selectedExercise = "relaxation"
            }
            ExerciseCard("👣 Step Counter Exercise", completedExercises.contains("step count"), cardGreen) {
                selectedExercise = "step count"
            }
        } else {
            when (selectedExercise) {
                "breathing" -> BreathingExercise(deepGreen)
                "grounding" -> GroundingExercise(deepGreen)
                "relaxation" -> MuscleRelaxationGuide(deepGreen)
                "step count" -> StepCounterWithGoal(deepGreen)

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
                colors = ButtonDefaults.buttonColors(containerColor = deepGreen)
            ) {
                Text("✅ Done! Back to Menu", color = Color.White)
            }
        }
    }
}

@Composable
fun ExerciseCard(title: String, isCompleted: Boolean = false, backgroundColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(20.dp)
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
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                modifier = Modifier.weight(1f)
            )
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Completed",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun BreathingExercise(textColor: Color) {
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
            style = MaterialTheme.typography.titleLarge.copy(color = textColor)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .size(180.dp)
                .graphicsLayer { scaleX = scale; scaleY = scale }
                .background(Color(0xFFB2DFDB), CircleShape)
                .shadow(15.dp, CircleShape)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Focus on your breathing and stay calm.",
            style = MaterialTheme.typography.bodyMedium.copy(color = textColor)
        )
    }
}

@Composable
fun GroundingExercise(deepGreen: Color) {
    val prompts = listOf(
        "👀  5 things you can see:",
        "✋  4 things you can touch:",
        "👂  3 things you can hear:",
        "👃  2 things you can smell:",
        "👅  1 thing you can taste:"
    )
    val responses = remember { mutableStateListOf("", "", "", "", "") }

    Column(modifier = Modifier.fillMaxWidth()) {
        prompts.forEachIndexed { index, prompt ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD0F0C0) // calming green tint
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF2E7D32))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = responses[index],
                        onValueChange = { responses[index] = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF388E3C),
                            unfocusedBorderColor = Color(0xFF81C784),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }
        }
    }
}
@Composable
fun MuscleRelaxationGuide(deepGreen: Color) {
    val steps = listOf(
        Pair("🧍‍♀️", "Tense your shoulders... hold... now relax."),
        Pair("✊", "Clench your fists... hold... now relax."),
        Pair("🧘", "Tighten your stomach... hold... now relax."),
        Pair("🦵", "Press your legs together... hold... now relax."),
        Pair("🙂", "Scrunch your face... hold... now relax.")
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        steps.forEach { (emoji, text) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFA5D6A7))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = emoji,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF1B5E20))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Breathe deeply between each step. You're doing great!",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF2E7D32)),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@SuppressLint("ServiceCast")
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun StepCounterWithGoal(deepGreen: Color) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val stepDetectorSensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) }
    var totalSteps by remember { mutableFloatStateOf(0f) }
    var stepGoal by remember { mutableIntStateOf(1000) }
    var goalReached by remember { mutableStateOf(false) }
    var goalSetMessage by remember { mutableStateOf(false) }

    val activity = context as? Activity
    val permission = android.Manifest.permission.ACTIVITY_RECOGNITION


    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(permission), 0)
        }
    }

    // Handle goal reached vibration
    LaunchedEffect(goalReached) {
        if (goalReached) {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                manager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))

            Toast.makeText(context, "Goal reached! 🎉", Toast.LENGTH_LONG).show()
        }
    }

    // Sensor listener
    DisposableEffect(Unit) {
        val stepListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
                    totalSteps += 1f

                    if (!goalReached && totalSteps >= stepGoal) {
                        goalReached = true
                    }

                    Log.d("StepDebug", "Step detected! Total: $totalSteps")
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        stepDetectorSensor?.let {
            sensorManager.registerListener(stepListener, it, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager.unregisterListener(stepListener)
        }
    }

    // UI
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "🚶 Step Counter",
            style = MaterialTheme.typography.headlineSmall.copy(color = deepGreen)
        )

        Spacer(modifier = Modifier.height(16.dp))

        var goalInput by remember { mutableStateOf("") }

        Text(
            text = "Set your step goal:",
            style = MaterialTheme.typography.bodyLarge.copy(color = deepGreen)
        )

        OutlinedTextField(
            value = goalInput,
            onValueChange = { goalInput = it },
            label = { Text("Step Goal") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                goalInput.toIntOrNull()?.let {
                    stepGoal = it
                    goalReached = false
                    totalSteps = 0f
                    goalSetMessage = true
                }
                goalInput = ""
            },
            colors = ButtonDefaults.buttonColors(containerColor = deepGreen)
        ) {
            Text("Set Goal", color = Color.White)
        }


        if (goalSetMessage) {
            LaunchedEffect(goalSetMessage) {
                Toast.makeText(context, "Step goal set!", Toast.LENGTH_SHORT).show()
                delay(2000)
                goalSetMessage = false
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "You’ve taken ${totalSteps.toInt()} steps.",
            style = MaterialTheme.typography.bodyLarge.copy(color = deepGreen)
        )

        if (goalReached) {
            Spacer(modifier = Modifier.height(24.dp))
            AnimatedStepGoalReached(deepGreen)
        } else {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Keep going to hit your step goal!",
                style = MaterialTheme.typography.bodyMedium.copy(color = deepGreen)
            )
        }
    }
}



@Composable
fun AnimatedStepGoalReached(color: Color) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GoalReachedAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .graphicsLayer(alpha = alpha)
            .background(color.copy(alpha = 0.1f), shape = RoundedCornerShape(16.dp))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🎉 Congratulations! You've reached your step goal!",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = color,
                fontWeight = FontWeight.Bold
            )
        )
    }
}