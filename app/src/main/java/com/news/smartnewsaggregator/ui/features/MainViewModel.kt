package com.news.smartnewsaggregator.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.news.smartnewsaggregator.domain.model.Category
import com.news.smartnewsaggregator.domain.model.NewsArticle
import com.news.smartnewsaggregator.domain.model.SortOrder
import com.news.smartnewsaggregator.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed class MainScreenState {
    object Loading : MainScreenState()
    data class Success(val articles: List<NewsArticle>) : MainScreenState()
    data class Error(val message: String) : MainScreenState()
    object Empty : MainScreenState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    private var currentPage = 1

    private val _sortOrder = MutableStateFlow(SortOrder.DATE_DESC)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _category = MutableStateFlow(Category.ALL)
    val category: StateFlow<Category> = _category.asStateFlow()

    init {
        observeNews()
        refreshNews()
    }

    private fun observeNews() {
        newsRepository.getNewsArticles()
            .combine(_sortOrder) { articles, sortOrder ->
                when (sortOrder) {
                    SortOrder.DATE_DESC -> articles.sortedByDescending { it.publishedAt }
                    SortOrder.DATE_ASC -> articles.sortedBy { it.publishedAt }
                    SortOrder.SOURCE_ASC -> articles.sortedBy { it.source }
                    SortOrder.SOURCE_DESC -> articles.sortedByDescending { it.source }
                }
            }
            .onEach { articles ->
                _state.value = if (articles.isEmpty()) MainScreenState.Empty else MainScreenState.Success(articles)
            }
            .launchIn(viewModelScope)
    }

    fun onPullToRefresh() {
        currentPage = 1
        refreshNews()
    }

    private fun refreshNews() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                newsRepository.refreshNews(currentPage, _category.value)
            } catch (e: IOException) {
                if (_state.value is MainScreenState.Empty || _state.value is MainScreenState.Loading) {
                    _state.value = MainScreenState.Error("No internet connection. Please check your connection and try again.")
                } else {
                    _errorChannel.send("Refresh failed: You're viewing offline content.")
                }
            } catch (e: Exception) {
                if (_state.value is MainScreenState.Empty || _state.value is MainScreenState.Loading) {
                    _state.value = MainScreenState.Error(e.message ?: "An unknown error occurred")
                } else {
                    _errorChannel.send("Refresh failed: An unknown error occurred.")
                }
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun loadMoreItems() {
        if (_isLoadingMore.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            currentPage++
            try {
                newsRepository.loadMoreNews(currentPage, _category.value)
            } catch (e: Exception) {
                currentPage--
                viewModelScope.launch { _errorChannel.send("Failed to load more news.") }
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    fun onToggleFavorite(articleId: String) {
        viewModelScope.launch {
            newsRepository.toggleFavorite(articleId)
        }
    }

    fun onArticleClicked(articleId: String) {
        viewModelScope.launch {
            newsRepository.markAsRead(articleId)
        }
    }

    fun onSortOrderChange(sortOrder: SortOrder) {
        _sortOrder.value = sortOrder
    }

    fun onCategoryChange(category: Category) {
        _category.value = category
        onPullToRefresh()
    }
}
