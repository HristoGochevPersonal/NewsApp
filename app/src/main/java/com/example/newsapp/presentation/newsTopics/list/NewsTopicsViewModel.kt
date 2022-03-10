package com.example.newsapp.presentation.newsTopics.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.presentation.articles.history.ArticlesHistorySharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// News topics view model
class NewsTopicsViewModel : ViewModel() {
    private val _newsTopicsStateFlow =
        MutableStateFlow<List<NewsTopicModel>>(listOf())
    val newsTopicsStateFlow = _newsTopicsStateFlow.asStateFlow()

    private val _articlesReadStateFlow =
        MutableStateFlow(ArticlesHistorySharedPreferences.fetch().size)
    val articlesReadStateFlow = _articlesReadStateFlow.asStateFlow()

    // Refresh the topics with info from shared preferences
    fun refreshTopics() {
        viewModelScope.launch(Dispatchers.IO) {
            val topics = NewsTopicsSharedPreferences.fetch()
            _newsTopicsStateFlow.emit(topics)
        }
    }

    // Updates the read articles counter
    fun refreshArticlesRead() {
        viewModelScope.launch(Dispatchers.IO) {
            val articlesRead = ArticlesHistorySharedPreferences.fetch().size
            _articlesReadStateFlow.emit(articlesRead)
        }
    }
}