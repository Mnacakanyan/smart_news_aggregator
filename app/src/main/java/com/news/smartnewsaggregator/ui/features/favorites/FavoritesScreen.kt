package com.news.smartnewsaggregator.ui.features.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.news.smartnewsaggregator.ui.features.components.NewsListItem
import com.news.smartnewsaggregator.ui.navigation.Screen

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.collectAsState(initial = emptyList())

    if (favorites.isEmpty()) {
        EmptyFavoritesScreen()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(favorites) { article ->
                NewsListItem(
                    article = article,
                    onToggleFavorite = { viewModel.onToggleFavorite(article.id) },
                    onItemClick = { navController.navigate(Screen.Detail.createRoute(article.url)) }
                )
            }
        }
    }
}

@Composable
private fun EmptyFavoritesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "No items"
        )
    }
}