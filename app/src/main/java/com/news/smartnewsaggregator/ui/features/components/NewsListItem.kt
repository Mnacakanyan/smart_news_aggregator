package com.news.smartnewsaggregator.ui.features.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.news.smartnewsaggregator.R
import com.news.smartnewsaggregator.domain.model.NewsArticle

@Composable
fun NewsListItem(
    article: NewsArticle,
    onToggleFavorite: (String) -> Unit,
    onItemClick: () -> Unit
) {
    val textColor = if (article.isRead) Color.LightGray else Color.Unspecified

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick() }
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(text = article.title, style = MaterialTheme.typography.titleMedium, color = textColor)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = article.description ?: "", style = MaterialTheme.typography.bodyMedium, maxLines = 3, color = textColor)
            }
            Icon(
                imageVector = if (article.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                modifier = Modifier.clickable { onToggleFavorite(article.id) }
            )
        }
    }
}
