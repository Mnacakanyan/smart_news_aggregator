package com.news.smartnewsaggregator.ui.features.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.news.smartnewsaggregator.domain.model.NewsArticle
import com.news.smartnewsaggregator.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val articleId: String = savedStateHandle.get<String>("articleId")!!

    val article: Flow<NewsArticle?> = newsRepository.getArticle(articleId)
}
