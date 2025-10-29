package com.news.smartnewsaggregator.domain.repository

import com.news.smartnewsaggregator.domain.model.Category
import com.news.smartnewsaggregator.domain.model.NewsArticle
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNewsArticles(): Flow<List<NewsArticle>>

    fun getFavoriteArticles(): Flow<List<NewsArticle>>

    fun getArticle(articleId: String): Flow<NewsArticle?>

    suspend fun refreshNews(page: Int, category: Category)

    suspend fun loadMoreNews(page: Int, category: Category)

    suspend fun toggleFavorite(articleId: String)

    suspend fun markAsRead(articleId: String)
}
