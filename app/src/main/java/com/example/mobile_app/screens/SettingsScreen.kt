package com.example.mobile_app.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.EmailAuthProvider

@Composable
fun SettingsScreen() {
    val primaryGreen = Color(0xFF388E3C)
    val lightGreenBackground = Color(0xFFE6F7E1)

    var oldPassword by remember { mutableStateOf("") }
    var newUsername by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var updateMessage by remember { mutableStateOf("") }

    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = lightGreenBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Update Account",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Old password field
                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = { Text("Old Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = newUsername,
                        onValueChange = { newUsername = it },
                        label = { Text("New Username") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = "Toggle password visibility"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    )

                    if (isLoading) {
                        CircularProgressIndicator(color = primaryGreen)
                    } else {
                        Button(
                            onClick = {
                                isLoading = true
                                // Pass the old password to validate it before making updates
                                updateUserProfile(oldPassword, newUsername, newPassword) { _, message ->
                                    isLoading = false
                                    updateMessage = message
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Update", color = Color.White)
                        }
                    }

                    if (updateMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = updateMessage,
                            fontSize = 14.sp,
                            color = if (updateMessage.contains("failed", true)) MaterialTheme.colorScheme.error else primaryGreen
                        )
                    }
                }
            }
        }
    }
}
fun updateUserProfile(oldPassword: String, username: String, password: String, callback: (Boolean, String) -> Unit) {
    val user = FirebaseAuth.getInstance().currentUser

    if (user == null) {
        callback(false, "No user logged in.")
        return
    }

    // Step 1: Re-authenticate the user with their old password
    val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)

    // Re-authenticate with the old password
    user.reauthenticate(credential).addOnCompleteListener { authTask ->
        if (authTask.isSuccessful) {
            // Step 2: Update the username (display name)
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()

            var updateSuccess = true
            var message = "Profile updated successfully."

            user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    updateSuccess = false
                    message = "Failed to update username."
                }
            }

            // Step 3: Update the password
            if (password.isNotEmpty()) {
                user.updatePassword(password).addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        updateSuccess = false
                        message = "Failed to update password."
                    }
                }
            }

            callback(updateSuccess, message)
        } else {
            callback(false, "Old password is incorrect.")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen()
}
