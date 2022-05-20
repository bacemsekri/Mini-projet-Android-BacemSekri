package com.mbds.bmp.newsletter.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mbds.bmp.newsletter.model.Article
import java.io.Serializable

@Dao
interface ArticleDao : Serializable {
    @Query("SELECT * FROM article ORDER BY publishedAt DESC")
    fun getArticles(): List<Article>

    @Query("SELECT * FROM article WHERE url = :url LIMIT 1")
    fun searchArticle(url: String): Article?

    @Insert
    fun insertArticle(vararg article: Article)

    @Delete
    fun deleteArticle(vararg article: Article)

}