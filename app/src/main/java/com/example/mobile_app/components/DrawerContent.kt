package com.example.mobile_app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppDrawer(onItemClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(250.dp)
            .background(Color(0xFF6E8B63)) // Light green background
            .padding(vertical = 24.dp)
    ) {
        Text(
            text = "Menu",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp, bottom = 24.dp)
        )

        DrawerItem(Icons.Default.Person, "User", onClick = { onItemClick("User") })
        DrawerItem(Icons.Default.Settings, "Settings", onClick = { onItemClick("Settings") })
        DrawerItem(Icons.AutoMirrored.Filled.ExitToApp, "Log Out", onClick = { onItemClick("Log Out") })
        DrawerItem(Icons.Default.Info, "About Us", onClick = { onItemClick("About Us") })

    }
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Icon(icon, contentDescription = label, tint = Color.White)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, color = Color.White, fontSize = 16.sp)
    }
}
