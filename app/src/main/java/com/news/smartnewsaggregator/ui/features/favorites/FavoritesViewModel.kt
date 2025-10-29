package com.news.smartnewsaggregator.ui.features.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.news.smartnewsaggregator.domain.model.NewsArticle
import com.news.smartnewsaggregator.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val favorites: Flow<List<NewsArticle>> = newsRepository.getFavoriteArticles()

    fun onToggleFavorite(articleId: String) {
        viewModelScope.launch {
            newsRepository.toggleFavorite(articleId)
        }
    }
}
