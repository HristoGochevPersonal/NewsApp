package com.example.newsapp.presentation.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

// Utilities that help with date formatting

val dateParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
val dateFormatter: DateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.US)

fun SimpleDateFormat.parseOrNull(string:String):Date?{
    return try{
        parse(string)
    }catch(e:Exception){
        e.printStackTrace()
        null
    }
}
fun DateFormat.formatOrNull(date:Date):String?{
    return try{
        format(date)
    }catch(e:Exception){
        e.printStackTrace()
        null
    }
}