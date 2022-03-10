package com.example.newsapp.presentation.newsTopics.search

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

// Search shared preferences singleton
object SearchSharedPreferences {

    private lateinit var sharedPreferences: SharedPreferences

    // Needs initialization ( Preferably on the application level )
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("searches", Context.MODE_PRIVATE)
    }

    // Fetches the search history
    fun fetch(): List<SearchItem> {
        val serializedObject = sharedPreferences.getString("searches", null) ?: return emptyList()
        val gson = Gson()
        val type: Type = object : TypeToken<List<SearchItem>>() {}.type
        return gson.fromJson(serializedObject, type)
    }

    // Saves the search history
    fun save(searchHistory: List<SearchItem>): Boolean {
        val gson = Gson()
        val json = gson.toJson(searchHistory)

        return sharedPreferences.edit().apply {
            putString("searches", json)
        }.commit()
    }

    // clears the search history
    fun clear(): Boolean = sharedPreferences.edit().remove("searches").commit()
}