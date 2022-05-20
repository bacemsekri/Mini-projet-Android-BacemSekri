package com.mbds.bmp.newsletter.model

import java.io.Serializable

data class ArticleNewsApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
) : Serializable