package com.example.newsapp.presentation.articles.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.presentation.articles.ArticleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Articles history view model
class ArticlesHistoryViewModel : ViewModel() {
    private val _articlesStateFlow = MutableStateFlow<List<ArticleModel>>(listOf())
    val articlesStateFlow = _articlesStateFlow.asStateFlow()

    // Refreshes in descending order
    fun refreshHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val articles = ArticlesHistorySharedPreferences.fetch().reversed()
            _articlesStateFlow.emit(articles)
        }
    }

    // Deletes the whole history and refreshes in descending order
    fun deleteHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            ArticlesHistorySharedPreferences.clear()
            val articles = ArticlesHistorySharedPreferences.fetch().reversed()
            _articlesStateFlow.emit(articles)
        }
    }

    // Deletes only one article and refreshes in descending order
    fun deleteHistoryArticle(article: ArticleModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val articles = ArticlesHistorySharedPreferences.fetch().toMutableList()
            articles.remove(article)
            ArticlesHistorySharedPreferences.save(articles)
            _articlesStateFlow.emit(articles)
        }
    }
}