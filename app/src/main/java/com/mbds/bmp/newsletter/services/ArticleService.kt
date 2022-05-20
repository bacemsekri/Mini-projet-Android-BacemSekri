package com.mbds.bmp.newsletter.services

import android.os.Parcelable
import com.mbds.bmp.newsletter.model.Article

interface ArticleService : Parcelable {
    suspend fun getArticles(page: Int): List<Article>?

    fun getTitleId(): Int

    fun getPageNumber(): Int

    val isFavorite: Boolean
}