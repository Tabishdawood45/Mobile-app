package com.example.mobile_app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AuthScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to the App", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("login_screen") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Already a Member? Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("signup_screen") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "New Here? Sign Up")
        }
    }
}
