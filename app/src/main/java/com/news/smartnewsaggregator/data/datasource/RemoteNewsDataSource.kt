package com.news.smartnewsaggregator.data.datasource

import com.news.smartnewsaggregator.data.remote.api.NewsApi
import com.news.smartnewsaggregator.data.remote.model.NewsArticleDto
import com.news.smartnewsaggregator.domain.model.Category
import javax.inject.Inject

interface RemoteNewsDataSource {
    suspend fun getNews(page: Int, category: Category): List<NewsArticleDto>
}

class RemoteNewsDataSourceImpl @Inject constructor(
    private val newsApi: NewsApi
) : RemoteNewsDataSource {

    override suspend fun getNews(page: Int, category: Category): List<NewsArticleDto> {
        val categoryValue = if (category == Category.ALL) null else category.value
        return newsApi.getNews(page = page, category = categoryValue).articles
    }
}
