package com.mbds.bmp.newsletter.services

import com.mbds.bmp.newsletter.dao.ArticleDao
import com.mbds.bmp.newsletter.model.Article

class FavoriteService(private val articleDao: ArticleDao) {
    fun exist(article: Article): Boolean {
        val result = articleDao.searchArticle(article.url)
        return result != null
    }


    fun add(article: Article) {
        if (!exist(article))
            articleDao.insertArticle(article)
    }

    fun delete(article: Article) {
        articleDao.deleteArticle(article)
    }
}