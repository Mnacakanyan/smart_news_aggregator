package com.news.smartnewsaggregator.data.remote.model

import com.google.gson.annotations.SerializedName

data class NewsArticleDto(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("urlToImage") val imageUrl: String?,
    @SerializedName("author") val author: String?,
    @SerializedName("publishedAt") val publishedAt: String?,
    @SerializedName("source") val source: SourceDto?,
    @SerializedName("url") val url: String?
)

data class SourceDto(
    @SerializedName("name") val name: String?
)
