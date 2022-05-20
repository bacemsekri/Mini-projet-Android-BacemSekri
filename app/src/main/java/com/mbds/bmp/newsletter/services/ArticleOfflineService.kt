package com.mbds.bmp.newsletter.services

import android.os.Parcel
import android.os.Parcelable
import com.mbds.bmp.newsletter.R
import com.mbds.bmp.newsletter.dao.ArticleDao
import com.mbds.bmp.newsletter.model.Article

class ArticleOfflineService(private val articleDao: ArticleDao) : ArticleService {

    constructor(parcel: Parcel) : this(parcel.readSerializable() as ArticleDao)

    override suspend fun getArticles(page: Int): List<Article>? = articleDao.getArticles()


    override fun getTitleId() = R.string.favorites

    override fun getPageNumber(): Int = 1

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(articleDao)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArticleOfflineService> {
        override fun createFromParcel(parcel: Parcel): ArticleOfflineService {
            return ArticleOfflineService(parcel)
        }

        override fun newArray(size: Int): Array<ArticleOfflineService?> {
            return arrayOfNulls(size)
        }
    }

    override val isFavorite: Boolean = true
}