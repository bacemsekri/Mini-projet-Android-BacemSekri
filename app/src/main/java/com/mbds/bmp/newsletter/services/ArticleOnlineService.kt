package com.mbds.bmp.newsletter.services

import android.os.Parcel
import android.os.Parcelable
import com.mbds.bmp.newsletter.R
import com.mbds.bmp.newsletter.model.Article
import com.mbds.bmp.newsletter.model.Category
import com.mbds.bmp.newsletter.model.Country
import com.mbds.bmp.newsletter.model.Editor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArticleOnlineService(
    val category: Category,
    val country: Country,
    val editors: List<Editor>
) :
    ArticleService {
    private val service: RetrofitApiService
    private var nbItems = -1

    constructor(parcel: Parcel) : this(
        parcel.readSerializable() as Category,
        parcel.readSerializable() as Country,
        (parcel.readSerializable() as ArrayList<Editor>).toList()
    ) {
        nbItems = parcel.readInt()
    }

    init {
        val retrofit = buildClient()
        //Init the api service with retrofit
        service = retrofit.create(RetrofitApiService::class.java)
    }

    /**
     * Configure retrofit
     */
    private fun buildClient(): Retrofit {
        val httpClient = OkHttpClient.Builder().apply {
            addLogInterceptor(this)
            addApiInterceptor(this)
        }.build()
        return Retrofit
            .Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    /**
     * Add a logger to the client so that we log every request
     */
    private fun addLogInterceptor(builder: OkHttpClient.Builder) {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        builder.addNetworkInterceptor(httpLoggingInterceptor)
    }

    /**
     * Intercept every request to the API and automatically add
     * the api key as query parameter
     */
    private fun addApiInterceptor(builder: OkHttpClient.Builder) {
        builder.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val originalHttpUrl = original.url
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("apikey", apiKey)
                    .build()

                val requestBuilder = original.newBuilder()
                    .url(url)
                val request = requestBuilder.build()
                return chain.proceed(request)
            }
        })
    }

    override suspend fun getArticles(page: Int): List<Article>? {
        val result = if (editors.isNotEmpty()) {
            service.getArticles(editors.map { editor -> editor.id ?: "" }
                .reduce { e1, e2 -> "$e1,$e2" }, page).execute()
        } else {
            service.getArticles(category.key ?: "", country.countryCode?.name ?: "").execute()
        }

        val articleNewsApiResponse = result.body()
        val articles = articleNewsApiResponse?.articles

        if (articles != null) {
            nbItems = articleNewsApiResponse.totalResults
        }

        return articles
    }

    override fun getTitleId(): Int = R.string.results

    override fun getPageNumber(): Int {
        if (nbItems < 0)
            return 1
        else {
            return nbItems / pageSize + if (nbItems % pageSize > 0) 1 else 0
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(category)
        parcel.writeSerializable(country)
        parcel.writeSerializable(ArrayList<Editor>().apply { addAll(editors) })
        parcel.writeInt(nbItems)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArticleOnlineService> {

        private const val apiKey = "63f085dd32274d2daaa83d357bfb74a4"
        private const val apiUrl = "https://newsapi.org/v2/"
        private const val pageSize = 20

        override fun createFromParcel(parcel: Parcel): ArticleOnlineService {
            return ArticleOnlineService(parcel)
        }

        override fun newArray(size: Int): Array<ArticleOnlineService?> {
            return arrayOfNulls(size)
        }
    }

    override val isFavorite: Boolean = false

}