package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.ui.home.model.LeaderboardItem
import com.antsfamily.danskflashcards.ui.home.model.mapToColor
import com.antsfamily.danskflashcards.ui.home.model.mapToIcon

@Composable
fun LeaderboardIcon(
    modifier: Modifier = Modifier,
    item: LeaderboardItem
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        item.mapToIcon()?.let {
            Image(
                painter = painterResource(it),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = item.place.toString(),
            color = item.mapToColor(),
            modifier = modifier.padding(top = if (item.place <= 3) 12.dp else 0.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LeaderboardIconPreview(modifier: Modifier = Modifier) {
    Column(modifier.padding(top = 48.dp)) {
        LeaderboardIcon(item = LeaderboardItem(name = "John Doe", score = 25, index = 0))
        LeaderboardIcon(item = LeaderboardItem(name = "John Doe", score = 25, index = 1))
        LeaderboardIcon(item = LeaderboardItem(name = "John Doe", score = 25, index = 2))
        LeaderboardIcon(item = LeaderboardItem(name = "John Doe", score = 25, index = 8))
        LeaderboardIcon(item = LeaderboardItem(name = "John Doe", score = 25, index = 17))
    }

}