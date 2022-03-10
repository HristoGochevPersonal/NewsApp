package com.example.newsapp.presentation.articles

import android.os.Parcelable
import com.example.newsapp.presentation.articles.ArticleModel
import kotlinx.parcelize.Parcelize

// News data model
@Parcelize
data class NewsModel(
    val name: String,
    val totalArticles: Int,
    val articles: List<ArticleModel>,
): Parcelable
