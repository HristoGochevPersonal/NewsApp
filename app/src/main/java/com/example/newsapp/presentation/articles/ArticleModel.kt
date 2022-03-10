package com.example.newsapp.presentation.articles

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Article data model
@Parcelize
data class ArticleModel(
    val source: SourceModel,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String,
):Parcelable
