package com.example.newsapp.presentation.articles.history

import android.content.Context
import android.content.SharedPreferences
import com.example.newsapp.presentation.articles.ArticleModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

// Articles history shared preference singleton
object ArticlesHistorySharedPreferences {

    private lateinit var sharedPreferences: SharedPreferences

    // Needs initialization ( Preferably on the application level )
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("articles", Context.MODE_PRIVATE)
    }

    // Fetches the articles history
    fun fetch(): List<ArticleModel> {
        val serializedObject =
            sharedPreferences.getString("articles", null) ?: return emptyList()
        val gson = Gson()
        val type: Type = object : TypeToken<List<ArticleModel>>() {}.type
        return gson.fromJson(serializedObject, type)
    }

    // Saves the articles history
    fun save(articles: List<ArticleModel>): Boolean {
        val gson = Gson()
        val json = gson.toJson(articles)
        return sharedPreferences.edit().apply {
            putString("articles", json)
        }.commit()
    }

    // Clears the articles history
    fun clear(): Boolean = sharedPreferences.edit().remove("articles").commit()
}