package com.news.smartnewsaggregator.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.news.smartnewsaggregator.data.local.model.NewsArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM news_articles ORDER BY publishedAt DESC")
    fun getNewsArticles(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE isFavorite = 1 ORDER BY publishedAt DESC")
    fun getFavoriteArticles(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE id = :articleId")
    fun getArticle(articleId: String): Flow<NewsArticleEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<NewsArticleEntity>)

    @Query("UPDATE news_articles SET isFavorite = NOT isFavorite WHERE id = :articleId")
    suspend fun toggleFavorite(articleId: String)

    @Query("UPDATE news_articles SET isRead = 1 WHERE id = :articleId")
    suspend fun markAsRead(articleId: String)

    @Query("DELETE FROM news_articles")
    suspend fun clearAll()

}
