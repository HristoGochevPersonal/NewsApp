package com.example.newsapp.presentation.utils

import com.example.newsapp.core.entity.News
import com.example.newsapp.presentation.articles.ArticleModel
import com.example.newsapp.presentation.articles.SourceModel
import com.example.newsapp.presentation.articles.NewsModel

// Functions that help avoid boilerplate code specifically withing this project

// Converts a news data object to a news data model
fun newsToNewsModel(name: String, news: News): NewsModel {
    val articles = news.articles.map { article ->
        val sourceModel = SourceModel(article.source.id, article.source.name)
        ArticleModel(
            sourceModel,
            article.author,
            article.title,
            article.description,
            article.url,
            article.urlToImage,
            article.publishedAt,
            article.content
        )
    }
    return NewsModel(name, news.totalResults, articles)
}