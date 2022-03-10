package com.example.newsapp.presentation.articles.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.presentation.articles.ArticleModel
import com.example.newsapp.presentation.articles.history.ArticlesHistorySharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleDetailsViewModel : ViewModel() {
    private val _readingContentsStateFlow = MutableStateFlow(false)
    val readingContentStateFlow = _readingContentsStateFlow.asStateFlow()

    // Toggles between reading description and contents
    fun toggleRead() {
        viewModelScope.launch(Dispatchers.IO) {
            val reading=!readingContentStateFlow.value
            _readingContentsStateFlow.emit( reading)
        }
    }


    // Updating the articles history
    fun updateArticlesHistory(article: ArticleModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val articlesRead = ArticlesHistorySharedPreferences.fetch().toMutableList()
            articlesRead.remove(article)
            articlesRead.add(article)
            ArticlesHistorySharedPreferences.save(articlesRead)
        }
    }
}