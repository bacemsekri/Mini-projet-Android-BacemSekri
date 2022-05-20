package com.mbds.bmp.newsletter.services

import com.mbds.bmp.newsletter.model.ArticleNewsApiResponse
import com.mbds.bmp.newsletter.model.SourceNewsApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApiService {
    //GET --> On lance une requête de type GET
    // everything est l'action du web service qu'on veut apeler
    // Elle sera concaténée avec l'url prédéfini dans retrofit
    @GET("top-headlines")
    fun getArticles(
        @Query("category") category: String = "",
        @Query("country") country: String = "",
        @Query("page") page: Int = 1,
    ): Call<ArticleNewsApiResponse>

    @GET("top-headlines")
    fun getArticles(
        @Query("sources") source: String = "",
        @Query("page") page: Int = 1,
    ): Call<ArticleNewsApiResponse>

    @GET("sources")
    fun getEditors(
        @Query("category") category: String = "",
        @Query("country") country: String = ""
    ): Call<SourceNewsApiResponse>
}