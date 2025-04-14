package com.example.mobile_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobile_app.auth.AuthManager
import com.example.mobile_app.auth.AuthScreen
import com.example.mobile_app.screens.HomeScreen
import com.example.mobile_app.screens.MoodReminderScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authManager = remember { AuthManager() }


    NavHost(
        navController = navController,
        startDestination = if (authManager.isUserLoggedIn()) "home_screen" else "auth"
    ) {
        composable("auth") {
            AuthScreen(authManager) {

                navController.navigate("home_screen") {
                    popUpTo("auth") { inclusive = true }
                }
            }
        }
        composable("home_screen") {
            HomeScreen(navController)
        }
        composable("mood_reminder_screen") {
            MoodReminderScreen()
        }
    }
}
