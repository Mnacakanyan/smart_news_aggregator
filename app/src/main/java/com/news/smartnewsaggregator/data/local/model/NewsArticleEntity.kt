package com.news.smartnewsaggregator.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.news.smartnewsaggregator.domain.model.NewsArticle

@Entity(tableName = "news_articles")
data class NewsArticleEntity(
    @PrimaryKey val id: String,
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

fun NewsArticleEntity.toDomain(): NewsArticle {
    return NewsArticle(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        author = author,
        publishedAt = publishedAt,
        source = source,
        url = url,
        isFavorite = isFavorite,
        isRead = isRead
    )
}
