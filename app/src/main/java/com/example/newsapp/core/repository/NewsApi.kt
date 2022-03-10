package com.example.newsapp.core.repository

import com.example.newsapp.core.entity.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Basic news articles api that requires a search query and an api key
interface NewsApi {
    @GET("everything")
    suspend fun getNews(@Query("q") topic:String,@Query("apiKey") key: String): Response<News>
}