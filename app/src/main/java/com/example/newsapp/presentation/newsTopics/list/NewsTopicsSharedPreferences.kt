package com.example.newsapp.presentation.newsTopics.list

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


// News topics shared preferences singleton
object NewsTopicsSharedPreferences {

    private lateinit var sharedPreferences: SharedPreferences

    // Needs initialization ( Preferably on the application level )
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("topics", Context.MODE_PRIVATE)
    }

    // Fetches the standard topics
    fun fetch(): List<NewsTopicModel> {
        val serializedObject =
            sharedPreferences.getString("news_topics", null) ?: return emptyList()
        val gson = Gson()
        val type: Type = object : TypeToken<List<NewsTopicModel>>() {}.type
        return gson.fromJson(serializedObject, type)
    }

    // Saves the standard topics
    fun save(articles: List<NewsTopicModel>): Boolean {
        val gson = Gson()
        val json = gson.toJson(articles)
        return sharedPreferences.edit().apply {
            putString("news_topics", json)
        }.commit()
    }


    // Sets the default standard topics with uris to images
    fun setDefaults() {
        val list = mutableListOf<NewsTopicModel>().apply {
            add(
                NewsTopicModel(
                    "Tesla",
                    "https://allcarbrandslist.com/wp-content/uploads/2020/12/Tesla-Logo-1-1024x768.png"
                )
            )
            add(
                NewsTopicModel(
                    "Apple",
                    "https://1000logos.net/wp-content/uploads/2016/10/Apple-Logo-640x400.png"
                )
            )
            add(
                NewsTopicModel(
                    "Google",
                    "https://clipartcraft.com/images/google-logo-transparent.png"
                )
            )
            add(
                NewsTopicModel(
                    "Facebook",
                    "https://clipartcraft.com/images250_/facebook-logo-circle.png"
                )
            )
            add(
                NewsTopicModel(
                    "Netflix",
                    "https://www.freepnglogos.com/uploads/netflix-logo-circle-png-5.png"
                )
            )
            add(
                NewsTopicModel(
                    "Ubisoft",
                    "https://2.bp.blogspot.com/-VkV2iO1m_2I/VX88cArk2nI/AAAAAAAALN4/_4E-uK7CoBM/s200/Ubisoft_logo-e1371021963352.png"
                )
            )
            add(NewsTopicModel("Steam", "https://pic.onlinewebfonts.com/svg/img_224266.png"))
            add(
                NewsTopicModel(
                    "EA",
                    "https://logos-download.com/wp-content/uploads/2016/11/EA_logo_Electronic_Arts-700x700.png"
                )
            )
            add(
                NewsTopicModel(
                    "Crypto",
                    "https://freepngimg.com/download/bitcoin/59669-cryptocurrency-logo-ethereum-zazzle-bitcoin-hd-image-free-png.png"
                )
            )
        }

        save(list)
    }

    // Clears the standard topics
    fun clear(): Boolean = sharedPreferences.edit().remove("news_topics").commit()
}