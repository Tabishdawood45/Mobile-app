//package com.example.mobile_app
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.runtime.Composable
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.mobile_app.components.MoodCalendar
//import com.example.mobile_app.data.MoodEntry
//import com.example.mobile_app.screens.HomeScreen
//import com.example.mobile_app.screens.MoodReminderScreen
//import com.example.mobile_app.viewmodel.MoodViewModel

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?)
//    {
//        super.onCreate(savedInstanceState)
//        setContent {
//            val viewModel: MoodViewModel = viewModel()
//            MoodCalendar { date, mood, note ->
//                viewModel.saveMood(MoodEntry(date, mood, note))
//            }
//        }
//    }
//}

package com.example.mobile_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobile_app.screens.HomeScreen
import com.example.mobile_app.screens.MoodReminderScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation() // Call the AppNavigation composable to set up navigation
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home_screen") {
        composable("home_screen") {
            HomeScreen(navController)
        }
        composable("mood_reminder_screen") {
            MoodReminderScreen()
        }
    }
}
