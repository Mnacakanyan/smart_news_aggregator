package com.news.smartnewsaggregator.data.repository

import com.news.smartnewsaggregator.data.local.db.NewsDao
import com.news.smartnewsaggregator.data.remote.api.NewsApi
import com.news.smartnewsaggregator.data.remote.model.NewsArticleDto
import com.news.smartnewsaggregator.domain.model.NewsArticle
import com.news.smartnewsaggregator.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao
) : NewsRepository {

    override fun getNewsArticles(): Flow<List<NewsArticle>> {
        return newsDao.getNewsArticles().map { articles -> articles.map { it.toDomain() } }
    }

    override suspend fun refreshNews(page: Int) {
        val newsFromApi = newsApi.getNews(page).articles
        if (page == 1) {
            newsDao.clearAll()
        }
        newsDao.insertAll(newsFromApi.map { it.toEntity() })
    }

    override suspend fun loadMoreNews(page: Int) {
        val newsFromApi = newsApi.getNews(page).articles
        newsDao.insertAll(newsFromApi.map { it.toEntity() })
    }

    override suspend fun toggleFavorite(articleId: String) {
        newsDao.toggleFavorite(articleId)
    }
}

private fun NewsArticleDto.toEntity() = com.news.smartnewsaggregator.data.local.model.NewsArticleEntity(
    id = id ?: UUID.randomUUID().toString(),
    title = title ?: "",
    description = description,
    imageUrl = imageUrl,
    author = author,
    publishedAt = publishedAt ?: "",
    source = source?.name ?: ""
)

private fun com.news.smartnewsaggregator.data.local.model.NewsArticleEntity.toDomain() = NewsArticle(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    author = author,
    publishedAt = publishedAt,
    source = source,
    isFavorite = isFavorite
)
