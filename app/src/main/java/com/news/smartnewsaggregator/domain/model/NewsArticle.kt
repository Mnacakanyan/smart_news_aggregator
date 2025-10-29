package com.news.smartnewsaggregator.domain.model

data class NewsArticle(
    val id: String,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val author: String?,
    val publishedAt: String,
    val source: String,
    val url: String,
    val isFavorite: Boolean = false,
    val isRead: Boolean = false
)
