package com.news.smartnewsaggregator.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.news.smartnewsaggregator.data.local.model.NewsArticleEntity

@Database(entities = [NewsArticleEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
