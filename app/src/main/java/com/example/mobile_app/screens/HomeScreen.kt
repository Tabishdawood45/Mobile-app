package com.example.mobile_app.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.mobile_app.components.AppDrawer
import com.example.mobile_app.components.ReminderCard
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.mobile_app.auth.AuthManager
import com.example.mobile_app.ui.theme.AnimatedImageBanner
import java.time.LocalDateTime
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val primaryGreen = Color(0xFF2E7D32)
    val backgroundGreen = Color(0xFFE8F5E9)
    val cardGreen = Color(0xFFA5D6A7)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer { route ->
                scope.launch { drawerState.close() }
                when (route) {
                    "User" -> navController.navigate("user")
                    "Settings" -> navController.navigate("settings")
                    "About Us" -> navController.navigate("about")
                    "Log Out" -> {
                        AuthManager().logout(navController)
                        navController.navigate("login_screen") {
                            popUpTo("home_screen") { inclusive = true }
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Statera", color = Color.White) },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = primaryGreen),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    }
                )
            },
            containerColor = backgroundGreen
        ) { padding ->
            // Wrap all content in LazyColumn for full scrolling
            LazyColumn(
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding(),
                    bottom = 24.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    AnimatedImageBanner(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(RoundedCornerShape(24.dp))
                    )
                }

                item {
                    val hour = LocalDateTime.now().hour
                    val greeting = when {
                        hour < 12 -> "Good Morning ☀️"
                        hour in 12..17 -> "Good Afternoon 🌿"
                        else -> "Good Evening 🌙"
                    }

                    Text(greeting, style = MaterialTheme.typography.headlineSmall, color = primaryGreen)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "How are you feeling today?",
                        style = MaterialTheme.typography.titleMedium,
                        color = primaryGreen
                    )
                }

                item {
                    SectionHeader("Reminders", Icons.Default.Alarm)
                    ReminderCard(time = "12:00", message = "Don't forget to breathe 🌿")
                }

                item {
                    SectionHeader("Tips & Tools", Icons.Default.TipsAndUpdates)
                }

                // Grid inside LazyColumn
                items(1) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp), // limit height inside scrollable column
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
                    ) {
                        item {
                            QuickAccessButton("Exercises", Icons.Default.FitnessCenter, cardGreen) {
                                navController.navigate("exercises_screen")
                            }
                        }
                        item {
                            QuickAccessButton("Calendar", Icons.Default.CalendarToday, cardGreen) {
                                navController.navigate("mood_reminder_screen")
                            }
                        }
                        item {
                            QuickAccessButton("Articles",
                                Icons.AutoMirrored.Filled.Article, cardGreen) {
                                navController.navigate("articles_screen")
                            }
                        }
                        item {
                            QuickAccessButton("Add Note",
                                Icons.AutoMirrored.Filled.NoteAdd, cardGreen) {
                                navController.navigate("journal_note_screen")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = title, tint = Color(0xFF4CAF50))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF388E3C)
        )
    }
}

@Composable
fun ReminderCard(time: String, message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD1C4E9)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Reminder: $time", fontWeight = FontWeight.Bold, color = Color(0xFF4E342E))
            Text(text = message, color = Color(0xFF2E7D32))
        }
    }
}

@Composable
fun QuickAccessButton(label: String, icon: ImageVector, background: Color, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelLarge, color = Color.White)
        }
    }
}
