package com.example.mobile_app.ui.theme

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mobile_app.R
import kotlinx.coroutines.delay
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun AnimatedImageBanner(
    modifier: Modifier = Modifier,
    images: List<Int> = listOf(
        R.drawable.banner1,
        R.drawable.banner2,
        R.drawable.banner3,
        R.drawable.banner4
    ),
    slideDurationMs: Long = 3000L
) {
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(images) {
        while (true) {
            delay(slideDurationMs)
            currentIndex = (currentIndex + 1) % images.size
        }
    }

    Box(modifier = modifier.clip(RoundedCornerShape(16.dp))) {
        Crossfade(targetState = currentIndex, animationSpec = tween(600)) { index ->
            Image(
                painter = painterResource(id = images[index]),
                contentDescription = "Banner Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
