package com.example.newsapp.presentation.articles.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.core.repository.NewsRepository
import com.example.newsapp.presentation.articles.NewsModel
import com.example.newsapp.presentation.utils.newsToNewsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Articles view model
class ArticlesViewModel : ViewModel() {
    private val _articlesStateFlow = MutableStateFlow(NewsModel("", 0, listOf()))
    val articlesStateFlow = _articlesStateFlow.asStateFlow()

    // Updates the articles state flow with info from the Api using a Search query
    fun refreshArticles(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val news = NewsRepository.getNews(query)
            val newsModel =
                if (news != null) newsToNewsModel(query, news)
                else NewsModel(query, 0, listOf())
            _articlesStateFlow.emit(newsModel)
        }
    }
}