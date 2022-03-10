package com.example.newsapp.core.entity

// News data object
data class News(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)