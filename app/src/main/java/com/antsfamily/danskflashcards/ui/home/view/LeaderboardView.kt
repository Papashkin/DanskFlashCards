package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.ui.home.model.LeaderItem
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun LeaderboardView(
    modifier: Modifier = Modifier,
    items: List<LeaderItem>,
) {
    Column(modifier) {
        Text(
            stringResource(R.string.leaderboard_title),
            modifier.fillMaxWidth().padding(horizontal = Padding.huge),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.height(12.dp))
        if (items.isNotEmpty()) {
            LeaderboardContent(items)
        } else {
            EmptyLeaderboard()
        }
    }
}

@Composable
fun LeaderboardContent(items: List<LeaderItem>) {
    LazyColumn {
        items(items) { item ->
            LeaderboardCard(item = item)
        }
    }
}

@Composable
fun EmptyLeaderboard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.drawable.ic_empty),
            null,
            modifier = modifier.size(160.dp),
            alpha = 0.7f
        )
        Text(
            text = stringResource(R.string.leaderboard_is_empty),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = modifier.padding(Padding.large),
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LeaderboardViewPreview0(modifier: Modifier = Modifier) {
    LeaderboardView(items = emptyList())
}

@Preview(showBackground = true)
@Composable
fun LeaderboardViewPreview1(modifier: Modifier = Modifier) {
    LeaderboardView(
        items = listOf(
            LeaderItem(name = "John", surname = "Doe", score = 44, index = 0, isUser = false),
            LeaderItem(name = "Pablo", surname = "Escobar", score = 32, index = 1, isUser = false),
            LeaderItem(name = "Andrea", surname = "Corti", score = 29, index = 2, isUser = true)
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LeaderboardViewPreview2(modifier: Modifier = Modifier) {
    LeaderboardView(
        items = listOf(
            LeaderItem(name = "John", surname = "Doe", score = 44, index = 0, isUser = false),
            LeaderItem(name = "Michael", surname = "Pupsik", score = 32, index = 1, isUser = false),
            LeaderItem(name = "Andrea", surname = "Corti", score = 29, index = 2, isUser = false),
            LeaderItem(name = "Sophia", surname = "Mercer", score = 32, index = 3, isUser = false),
            LeaderItem(name = "Noah", surname = "Sinclair", score = 29, index = 4, isUser = false)
        )
    )
}
