package com.news.smartnewsaggregator.data.remote.api

import com.news.smartnewsaggregator.data.remote.model.NewsArticleDto
import retrofit2.http.GET
import retrofit2.http.Query

data class NewsResponse(
    val articles: List<NewsArticleDto>
)

interface NewsApi {
    @GET("v2/top-headlines?country=us") // Example endpoint
    suspend fun getNews(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 50,
        @Query("category") category: String? = null,
        @Query("apiKey") apiKey: String = "365f1219dd0c405c9803ea28f28c8de6"
    ): NewsResponse
}
