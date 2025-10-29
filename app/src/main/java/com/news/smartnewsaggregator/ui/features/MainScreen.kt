package com.news.smartnewsaggregator.ui.features

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.news.smartnewsaggregator.ui.features.components.FilterBar
import com.news.smartnewsaggregator.ui.features.components.NewsListItem
import com.news.smartnewsaggregator.ui.navigation.Screen
import kotlinx.coroutines.flow.collectLatest
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val listState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = viewModel::onPullToRefresh
    )

    val sortOrder by viewModel.sortOrder.collectAsState()
    val category by viewModel.category.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.errorChannel.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        FilterBar(
            selectedSortOrder = sortOrder,
            onSortOrderChange = viewModel::onSortOrderChange,
            selectedCategory = category,
            onCategoryChange = viewModel::onCategoryChange
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            when (val currentState = state) {
                is MainScreenState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is MainScreenState.Success -> {
                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        items(currentState.articles) { article ->
                            NewsListItem(
                                article = article,
                                onToggleFavorite = { viewModel.onToggleFavorite(article.id) },
                                onItemClick = {
                                    viewModel.onArticleClicked(article.id)
                                    val url = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                                    navController.navigate(Screen.Detail.createRoute(url))
                                }
                            )
                        }
                        item {
                            if (isLoadingMore) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }
                    }

                    val layoutInfo = listState.layoutInfo
                    val visibleItemsInfo = layoutInfo.visibleItemsInfo
                    if (visibleItemsInfo.isNotEmpty() && layoutInfo.totalItemsCount > 0) {
                        val lastVisibleItemIndex = visibleItemsInfo.last().index
                        if (lastVisibleItemIndex == layoutInfo.totalItemsCount - 1) {
                            LaunchedEffect(lastVisibleItemIndex) {
                                viewModel.loadMoreItems()
                            }
                        }
                    }
                }

                is MainScreenState.Error -> {
                    ErrorState(message = currentState.message, onRetry = viewModel::onPullToRefresh)
                }

                is MainScreenState.Empty -> {
                    Text(
                        text = "No news available.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = message, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}
