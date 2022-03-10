package com.example.newsapp.presentation

import android.app.Application
import com.example.newsapp.presentation.newsTopics.list.NewsTopicsSharedPreferences
import com.example.newsapp.presentation.articles.history.ArticlesHistorySharedPreferences
import com.example.newsapp.presentation.newsTopics.search.SearchSharedPreferences

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initializing all shared preferences storages
        SearchSharedPreferences.init(this)
        ArticlesHistorySharedPreferences.init(this)
        NewsTopicsSharedPreferences.init(this)
//        NewsTopicsSharedPreferences.clear()
        if (NewsTopicsSharedPreferences.fetch().count() == 0) {
            NewsTopicsSharedPreferences.setDefaults()
        }
    }
}