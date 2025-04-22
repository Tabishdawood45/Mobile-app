
package com.example.mobile_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_app.data.Article
import com.example.mobile_app.network.RetrofitInstance
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class ArticleViewModel : ViewModel() {
    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    private val _errorMessage = MutableStateFlow("")
//    val errorMessage: StateFlow<String> = _errorMessage

    private val apiKey = "pub_81507e187a31af174e196e7c8d804ef1266d6"

    private val categoryMap = mapOf(
        "Health" to "Health tips",
        "Fitness" to "workout tips",
        "Exercise" to "exercise routines",

    )

    fun searchArticles(query: String) {
        val categoryQuery = categoryMap[query] ?: query
        _selectedCategory.value = query
        viewModelScope.launch {
            try {
                Log.d("ArticleViewModel", "Searching articles with query: $categoryQuery")
                val response = RetrofitInstance.api.searchNews(categoryQuery, apiKey)
                Log.d("ArticleViewModel", "API Response: ${response.results}")
                _articles.value = response.results ?: emptyList()
                _errorMessage.value = ""
            } catch (e: Exception) {
                Log.e("ArticleViewModel", "Error fetching articles", e)
                _errorMessage.value = "Error fetching articles. Please try again."
            }
        }
    }
}
