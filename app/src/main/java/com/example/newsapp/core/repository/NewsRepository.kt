package com.example.newsapp.core.repository

import com.example.newsapp.core.entity.News
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Represents the api as a repository
object NewsRepository {
    // Sets it up so any data obtained through it
    // Is automatically converted to a data object using Gson
    private val api: NewsApi by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    // Gets the news articles with a given search query
    suspend fun getNews(query: String): News? {
        val response = api.getNews(query, apiKey)
        if (!response.isSuccessful) {
            return null
        }
        return response.body()
    }
}