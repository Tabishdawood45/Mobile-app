

package com.example.mobile_app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobile_app.auth.AuthManager

@Composable
fun HomeScreen(navController: NavController) {
    val authManager = remember { AuthManager() }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Welcome to the Home Screen",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (authManager.isUserLoggedIn()) {
                navController.navigate("mood_reminder_screen")
            } else {
                showDialog = true
            }
        }) {
            Text("Go to Mood & Reminder")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (authManager.isUserLoggedIn()) {
            Button(onClick = {
                authManager.logout()
                navController.navigate("auth") {
                    popUpTo("home_screen") { inclusive = true }
                }
            }) {
                Text("Logout")
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Sign In Required") },
                text = { Text("Please sign in to use this feature.") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        navController.navigate("auth") {
                            popUpTo("home_screen") { inclusive = true }
                        }
                    }) {
                        Text("Sign In")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
