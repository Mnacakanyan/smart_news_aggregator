package com.news.smartnewsaggregator.di

import android.content.Context
import androidx.room.Room
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.news.smartnewsaggregator.data.datasource.LocalNewsDataSource
import com.news.smartnewsaggregator.data.datasource.LocalNewsDataSourceImpl
import com.news.smartnewsaggregator.data.datasource.RemoteNewsDataSource
import com.news.smartnewsaggregator.data.datasource.RemoteNewsDataSourceImpl
import com.news.smartnewsaggregator.data.local.db.AppDatabase
import com.news.smartnewsaggregator.data.local.db.NewsDao
import com.news.smartnewsaggregator.data.remote.api.NewsApi
import com.news.smartnewsaggregator.data.remote.interceptor.RetryInterceptor
import com.news.smartnewsaggregator.data.repository.NewsRepositoryImpl
import com.news.smartnewsaggregator.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(RetryInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApi(okHttpClient: OkHttpClient): NewsApi {
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizeBytes(200 * 1024 * 1024)
                    .build()
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "news_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(appDatabase: AppDatabase): NewsDao {
        return appDatabase.newsDao()
    }

    @Provides
    @Singleton
    fun provideRemoteNewsDataSource(newsApi: NewsApi): RemoteNewsDataSource {
        return RemoteNewsDataSourceImpl(newsApi)
    }

    @Provides
    @Singleton
    fun provideLocalNewsDataSource(newsDao: NewsDao): LocalNewsDataSource {
        return LocalNewsDataSourceImpl(newsDao)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(remoteNewsDataSource: RemoteNewsDataSource, localNewsDataSource: LocalNewsDataSource): NewsRepository {
        return NewsRepositoryImpl(remoteNewsDataSource, localNewsDataSource)
    }
}
