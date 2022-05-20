package com.mbds.bmp.newsletter.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class Article(
    val author: String?,
    val title: String,
    val description: String,
    @PrimaryKey
    val url: String,
    val urlToImage: String?,
    val publishedAt: Date,
    val content: String?,
    @Embedded val source: Source
) : BaseObservable(), Serializable {

    @Ignore
    @get:Bindable
    var isFavorite = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.favorite)
        }

    data class Source(val id: String?, val name: String) : Serializable
}