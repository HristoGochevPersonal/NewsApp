package com.example.newsapp.presentation.articles

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Article source data model
@Parcelize
data class SourceModel(
    val id: String?,
    val name: String
) : Parcelable
