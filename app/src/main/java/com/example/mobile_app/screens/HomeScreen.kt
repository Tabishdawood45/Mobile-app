package com.example.mobile_app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Welcome to the Home Screen", style = MaterialTheme.typography.headlineSmall) // Use headlineSmall or another valid style

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.navigate("mood_reminder_screen") }) {
            Text("Go to Mood & Reminder")
        }
    }
}
