package com.example.mobile_app.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobile_app.R
@Composable
fun AboutUsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.health_wellness),
            contentDescription = "About Us Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAboutUsScreen() {
    AboutUsScreen()
}
