package com.example.mobile_app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobile_app.auth.AuthManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SignupScreen(authManager: AuthManager, navController: NavController, navToHome: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    val primaryGreen = Color(0xFF388E3C)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 40.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Create Account 👤",
                style = MaterialTheme.typography.headlineMedium.copy(color = primaryGreen)
            )
            Text(
                text = "Sign up to get started",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            @Composable
            fun styledTextField(
                value: String,
                onValueChange: (String) -> Unit,
                label: String,
                isPassword: Boolean = false,
                trailingIcon: @Composable (() -> Unit)? = null
            ) = OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
                shape = MaterialTheme.shapes.large,
                visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = trailingIcon,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryGreen,
                    cursorColor = primaryGreen
                )
            )

            styledTextField(email, { email = it }, "Email")
            styledTextField(username, { username = it }, "Username")
            styledTextField(address, { address = it }, "Address")

            styledTextField(
                password,
                { password = it },
                "Password",
                isPassword = true,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = primaryGreen
                        )
                    }
                }
            )

            styledTextField(confirmPassword, { confirmPassword = it }, "Confirm Password", isPassword = true)

            Button(
                onClick = {
                    if (password != confirmPassword) {
                        message = "Passwords do not match"
                    } else {
                        authManager.signUp(email, password) { success, error ->
                            if (success) {
                                val user = FirebaseAuth.getInstance().currentUser
                                user?.let {
                                    val db = FirebaseFirestore.getInstance()
                                    val userProfile = hashMapOf(
                                        "email" to email,
                                        "username" to username,
                                        "address" to address
                                    )
                                    db.collection("users").document(user.uid)
                                        .set(userProfile)
                                        .addOnSuccessListener {
                                            navToHome()
                                            message = "Signup successful"
                                        }
                                        .addOnFailureListener { e ->
                                            message = "Error saving profile: ${e.message}"
                                        }
                                }
                            } else {
                                message = error
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryGreen,
                    contentColor = Color.White
                )
            ) {
                Text("Sign Up")
            }

            message?.let {
                Text(
                    text = it,
                    color = if (it == "Signup successful") primaryGreen else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Already have an account?", color = Color.Gray)
            TextButton(onClick = { navController.navigate("login_screen") }) {
                Text("Log In", color = primaryGreen)
            }
        }
    }
}
