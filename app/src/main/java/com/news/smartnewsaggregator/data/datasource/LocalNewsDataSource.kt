package com.news.smartnewsaggregator.data.datasource

import com.news.smartnewsaggregator.data.local.db.NewsDao
import com.news.smartnewsaggregator.data.local.model.NewsArticleEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalNewsDataSource {
    fun getNewsArticles(): Flow<List<NewsArticleEntity>>
    fun getFavoriteArticles(): Flow<List<NewsArticleEntity>>
    fun getArticle(articleId: String): Flow<NewsArticleEntity?>
    suspend fun insertAll(articles: List<NewsArticleEntity>)
    suspend fun toggleFavorite(articleId: String)
    suspend fun markAsRead(articleId: String)
    suspend fun clearAll()
}

class LocalNewsDataSourceImpl @Inject constructor(
    private val newsDao: NewsDao
) : LocalNewsDataSource {

    override fun getNewsArticles(): Flow<List<NewsArticleEntity>> = newsDao.getNewsArticles()

    override fun getFavoriteArticles(): Flow<List<NewsArticleEntity>> = newsDao.getFavoriteArticles()

    override fun getArticle(articleId: String): Flow<NewsArticleEntity?> = newsDao.getArticle(articleId)

    override suspend fun insertAll(articles: List<NewsArticleEntity>) = newsDao.insertAll(articles)

    override suspend fun toggleFavorite(articleId: String) = newsDao.toggleFavorite(articleId)

    override suspend fun markAsRead(articleId: String) = newsDao.markAsRead(articleId)

    override suspend fun clearAll() = newsDao.clearAll()
}
