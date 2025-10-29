package com.news.smartnewsaggregator.data.repository

import com.news.smartnewsaggregator.data.datasource.LocalNewsDataSource
import com.news.smartnewsaggregator.data.datasource.RemoteNewsDataSource
import com.news.smartnewsaggregator.data.remote.model.NewsArticleDto
import com.news.smartnewsaggregator.domain.model.Category
import com.news.smartnewsaggregator.domain.model.NewsArticle
import com.news.smartnewsaggregator.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteNewsDataSource: RemoteNewsDataSource,
    private val localNewsDataSource: LocalNewsDataSource
) : NewsRepository {

    override fun getNewsArticles(): Flow<List<NewsArticle>> {
        return localNewsDataSource.getNewsArticles().map { articles -> articles.map { it.toDomain() } }
    }

    override fun getFavoriteArticles(): Flow<List<NewsArticle>> {
        return localNewsDataSource.getFavoriteArticles().map { articles -> articles.map { it.toDomain() } }
    }

    override fun getArticle(articleId: String): Flow<NewsArticle?> {
        return localNewsDataSource.getArticle(articleId).map { it?.toDomain() }
    }

    override suspend fun refreshNews(page: Int, category: Category) {
        val newsFromApi = remoteNewsDataSource.getNews(page, category)
        if (page == 1) {
            localNewsDataSource.clearAll()
        }
        localNewsDataSource.insertAll(newsFromApi.map { it.toEntity() })
    }

    override suspend fun loadMoreNews(page: Int, category: Category) {
        val newsFromApi = remoteNewsDataSource.getNews(page, category)
        localNewsDataSource.insertAll(newsFromApi.map { it.toEntity() })
    }

    override suspend fun toggleFavorite(articleId: String) {
        localNewsDataSource.toggleFavorite(articleId)
    }

    override suspend fun markAsRead(articleId: String) {
        localNewsDataSource.markAsRead(articleId)
    }
}

private fun NewsArticleDto.toEntity() = com.news.smartnewsaggregator.data.local.model.NewsArticleEntity(
    id = id ?: UUID.randomUUID().toString(),
    title = title ?: "",
    description = description,
    imageUrl = imageUrl,
    author = author,
    publishedAt = publishedAt ?: "",
    url = url ?: "",
    source = source?.name ?: "",
    isRead = false
)

private fun com.news.smartnewsaggregator.data.local.model.NewsArticleEntity.toDomain() = NewsArticle(
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
