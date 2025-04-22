package com.example.mobile_app.data


data class NewsResponse(
    val results: List<Article>?
)

data class Article(
    val title: String?,
    val link: String?,
    val description: String?,
    val pubDate: String?,
    val urlToImage: String?
)
