package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.ui.home.model.LeaderboardItem
import com.antsfamily.danskflashcards.ui.home.model.LeaderboardModel
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun LeaderboardView(
    modifier: Modifier = Modifier,
    model: LeaderboardModel,
) {
    Column(modifier) {
        Text(
            stringResource(R.string.leaderboard_title),
            modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium,
        )
        LazyColumn {
            items(model.leaders.size) {
                LeaderboardItem(
                    item = if (model.user.index == it) model.user else model.leaders[it],
                    isUser = (model.user.index == it)
                )
            }
        }
        if (model.user.index == 3) {
            LeaderboardItem(item = model.user, isUser = true)
        }
        if (model.user.index > 3) {
            Text(
                ".....",
                modifier
                    .fillMaxWidth()
                    .padding(bottom = Padding.small),
                textAlign = TextAlign.Center,
                fontSize = FontSize.H4,
            )
            LeaderboardItem(item = model.user, isUser = true)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LeaderboardViewPreview1(modifier: Modifier = Modifier) {
    LeaderboardView(
        model = LeaderboardModel(
            leaders = listOf(
                LeaderboardItem(name = "John", surname = "Doe", score = 44, index = 0),
                LeaderboardItem(name = "Pablo", surname = "Escobar", score = 32, index = 1),
                LeaderboardItem(name = "Andrea", surname = "Corti", score = 29, index = 2),
            ),
            user = LeaderboardItem(name = "Pablo", surname = "Escobar", score = 32, index = 1),
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LeaderboardViewPreview2(modifier: Modifier = Modifier) {
    LeaderboardView(
        model = LeaderboardModel(
            leaders = listOf(
                LeaderboardItem(name = "John", surname = "Doe", score = 44, index = 0),
                LeaderboardItem(name = "Michael", surname = "Pupsik", score = 32, index = 1),
                LeaderboardItem(name = "Andrea", surname = "Corti", score = 29, index = 2),
            ),
            user = LeaderboardItem(name = "Pablo", surname = "Escobar", score = 12, index = 10),
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LeaderboardViewPreview3(modifier: Modifier = Modifier) {
    LeaderboardView(
        model = LeaderboardModel(
            leaders = listOf(
                LeaderboardItem(name = "John", surname = "Doe", score = 44, index = 0),
                LeaderboardItem(name = "Michael", surname = "Pupsik", score = 32, index = 1),
                LeaderboardItem(name = "Andrea", surname = "Corti", score = 29, index = 2),
            ),
            user = LeaderboardItem(name = "Pablo", surname = "Escobar", score = 27, index = 3),
        )
    )
}