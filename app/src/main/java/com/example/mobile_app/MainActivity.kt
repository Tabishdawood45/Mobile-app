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
import com.example.mobile_app.screens.*

import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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
        // Entry screen: choose login or signup
        composable("auth") {
            AuthScreen(navController)
        }

        // Login screen
        composable("login_screen") {
            LoginScreen(authManager, navController) {
                navController.navigate("home_screen") {
                    popUpTo("auth") { inclusive = true }
                }
            }
        }

        // Signup screen
        composable("signup_screen") {
            SignupScreen(authManager, navController) {
                navController.navigate("home_screen") {
                    popUpTo("auth") { inclusive = true }
                }
            }
        }

        // Home screen
        composable("home_screen") {
            HomeScreen(navController)
        }

        // Mood & Reminder screen
        composable("mood_reminder_screen") {
            MoodReminderScreen()
        }

        composable("exercises_screen") {
            ExercisesScreen()
        }

        composable("articles_screen") {
            ArticleScreen(onArticleClick = { url ->
                val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                navController.navigate("article_detail_screen/$encodedUrl")
            })
        }

        composable(
            "article_detail_screen/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url")?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            } ?: ""
            ArticleDetailScreen(url)
        }


        composable("journal_note_screen") {
            JournalNoteScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
        composable("about") {
            AboutUsScreen()
        }
        composable("user") {
            UserScreen(navController)
        }
        composable("mood_tracker_screen") {
            MoodTrackerScreen()
        }

    }
}
