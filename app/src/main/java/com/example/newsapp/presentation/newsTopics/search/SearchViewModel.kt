package com.example.newsapp.presentation.newsTopics.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Search history view model
class SearchViewModel : ViewModel() {

    private val _searchesStateFlow = MutableStateFlow<List<SearchItem>>(listOf())
    val searchesStateFlow = _searchesStateFlow.asStateFlow()

    // Refreshes the search history with info from the search history shared preferences
    fun refreshSearches() {
        viewModelScope.launch(Dispatchers.IO) {
            val searchHistory = SearchSharedPreferences.fetch().reversed()
            _searchesStateFlow.emit(searchHistory)
        }
    }

    // Updates the search history shared preferences with a new input
    fun updateSearches(query: String) {
        viewModelScope.launch(Dispatchers.IO) {

            if (query.isEmpty()) {
                return@launch
            }

            val searchItem = SearchItem(query)
            val searchHistory = SearchSharedPreferences.fetch().toMutableList()
            if (!searchHistory.remove(searchItem)) {
                if (searchHistory.size > 5) {
                    searchHistory.removeFirst()
                }
            }

            searchHistory.add(searchItem)

            SearchSharedPreferences.save(searchHistory)

            val searches = SearchSharedPreferences.fetch().reversed()

            _searchesStateFlow.emit(searches)
        }
    }

    // Clears the search history shared preferences
    fun clearSearches() {
        viewModelScope.launch(Dispatchers.IO) {
            SearchSharedPreferences.clear()
            _searchesStateFlow.emit(listOf())
        }
    }
}