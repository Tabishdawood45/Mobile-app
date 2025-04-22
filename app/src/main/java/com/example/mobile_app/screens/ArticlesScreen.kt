
package com.example.mobile_app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobile_app.viewmodel.ArticleViewModel
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ArticleScreen(viewModel: ArticleViewModel = viewModel()) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val articles by viewModel.articles.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Choose a category", style = MaterialTheme.typography.titleMedium)

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            listOf("Health", "Fitness", "Exercise").forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { viewModel.searchArticles(category) },
                    label = { Text(category) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(articles) { article ->
                ArticleCard(article)
            }
        }
    }
}

@Composable
fun ArticleCard(article: com.example.mobile_app.data.Article) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = article.title ?: "No Title", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = article.description ?: "No Description")
        }
    }
}
