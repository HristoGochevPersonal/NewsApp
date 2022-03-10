package com.example.newsapp.core.entity

// Articles data object
data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String,
)