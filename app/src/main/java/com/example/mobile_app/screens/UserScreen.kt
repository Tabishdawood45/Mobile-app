package com.example.mobile_app.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobile_app.auth.AuthManager
import com.example.mobile_app.auth.DeleteAccountDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
@Composable
fun UserScreen(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser
    val authManager = AuthManager()

    var username by remember { mutableStateOf("Loading...") }
    var address by remember { mutableStateOf("Loading...") }
    var isLoading by remember { mutableStateOf(true) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val darkGreen = Color(0xFF2E7D32)
    val lightGreen = Color(0xFFE8F5E9)
    val cardGreen = Color(0xFF388E3C)

    LaunchedEffect(user?.uid) {
        user?.let { currentUser ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        username = document.getString("username") ?: "No username"
                        address = document.getString("address") ?: "No address"
                    } else {
                        username = "No username"
                        address = "No address"
                    }
                    isLoading = false
                }
                .addOnFailureListener {
                    username = "Error loading username"
                    address = "Error loading address"
                    isLoading = false
                    Toast.makeText(context, "Failed to load user profile", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = lightGreen) {
        if (isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = darkGreen)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Loading profile...", color = darkGreen)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "User Profile",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkGreen,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                    elevation = CardDefaults.elevatedCardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        ProfileRow(Icons.Filled.Email, "Email", user?.email ?: "N/A", cardGreen)
                        ProfileRow(Icons.Filled.LocationOn, "Address", address, cardGreen)
                        ProfileRow(Icons.Filled.AccountCircle, "Username", username, cardGreen)

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { showDeleteDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Delete Account", color = Color.White)
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        DeleteAccountDialog(
            isVisible = showDeleteDialog,
            onDismiss = { showDeleteDialog = false },
            onDeleteConfirmed = { password ->
                authManager.deleteUserAccount(password) { success, message ->
                    if (success) {
                        Toast.makeText(context, "Account deleted", Toast.LENGTH_SHORT).show()
                        navController.navigate("login_screen") {
                            popUpTo("home_screen") { inclusive = true }
                        }
                    } else {
                        errorMessage = message
                        Toast.makeText(context, message ?: "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                    showDeleteDialog = false
                }
            }
        )
    }
}

@Composable
fun ProfileRow(icon: ImageVector, label: String, value: String, iconTint: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "$label: $value",
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}
