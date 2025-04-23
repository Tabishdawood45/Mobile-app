package com.example.mobile_app.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mobile_app.viewmodel.ArticleViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
@Composable
fun ArticleScreen(
    viewModel: ArticleViewModel = viewModel(),
    onArticleClick: (String) -> Unit
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val articles by viewModel.articles.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Choose a category",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF388E3C)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            listOf("Health", "Fitness", "Exercise").forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { viewModel.searchArticles(category) },
                    label = {
                        Text(
                            category,
                            color = if (selectedCategory == category) Color.White else Color(0xFF388E3C)
                        )
                    },
                    modifier = Modifier.height(48.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = if (selectedCategory == category) Color(0xFF388E3C) else Color(0xFFE8F5E9),
                        labelColor = if (selectedCategory == category) Color.White else Color(0xFF388E3C)
                    ),
                    enabled = true
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(articles) { article ->
                ArticleCard(article = article, onClick = {
                    article.link?.let { onArticleClick(it) }
                    println("Article URL: ${article.link}")
                })
            }
        }
    }
}

@Composable
fun ArticleCard(
    article: com.example.mobile_app.data.Article,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }, // Make card clickable
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = article.title ?: "No Title",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF388E3C)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.description ?: "No Description",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF616161))
            )
        }
    }
}
