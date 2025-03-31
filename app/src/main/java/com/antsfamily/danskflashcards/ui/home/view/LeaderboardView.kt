package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
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
import com.antsfamily.danskflashcards.ui.home.model.LeaderboardItem
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.grey_200

@Composable
fun LeaderboardView(
    modifier: Modifier = Modifier,
    model: LeaderboardItem,
) {
    Column(modifier) {
        Text(
            stringResource(R.string.leaderboard_title),
            modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium,
        )
        if (model.leaders.isNotEmpty()) {
            LeaderboardContent(modifier, model)
        } else {
            EmptyLeaderboard(modifier)
        }
    }
}

@Composable
fun LeaderboardContent(modifier: Modifier = Modifier, model: LeaderboardItem) {
    LazyColumn {
        items(model.leaders.size) {
            LeaderboardCard(
                item = if (model.user.index == it) model.user else model.leaders[it],
                isUser = (model.user.index == it)
            )
        }
    }
    if (model.user.index == 3) {
        LeaderboardCard(item = model.user, isUser = true)
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
        LeaderboardCard(item = model.user, isUser = true)
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
    LeaderboardView(
        model = LeaderboardItem(
            leaders = emptyList(),
            user = LeaderItem(name = "Pablo", surname = "Escobar", score = 32, index = 1),
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LeaderboardViewPreview1(modifier: Modifier = Modifier) {
    LeaderboardView(
        model = LeaderboardItem(
            leaders = listOf(
                LeaderItem(name = "John", surname = "Doe", score = 44, index = 0),
                LeaderItem(name = "Pablo", surname = "Escobar", score = 32, index = 1),
                LeaderItem(name = "Andrea", surname = "Corti", score = 29, index = 2),
            ),
            user = LeaderItem(name = "Pablo", surname = "Escobar", score = 32, index = 1),
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LeaderboardViewPreview2(modifier: Modifier = Modifier) {
    LeaderboardView(
        model = LeaderboardItem(
            leaders = listOf(
                LeaderItem(name = "John", surname = "Doe", score = 44, index = 0),
                LeaderItem(name = "Michael", surname = "Pupsik", score = 32, index = 1),
                LeaderItem(name = "Andrea", surname = "Corti", score = 29, index = 2),
            ),
            user = LeaderItem(name = "Pablo", surname = "Escobar", score = 12, index = 10),
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LeaderboardViewPreview3(modifier: Modifier = Modifier) {
    LeaderboardView(
        model = LeaderboardItem(
            leaders = listOf(
                LeaderItem(name = "John", surname = "Doe", score = 44, index = 0),
                LeaderItem(name = "Michael", surname = "Pupsik", score = 32, index = 1),
                LeaderItem(name = "Andrea", surname = "Corti", score = 29, index = 2),
            ),
            user = LeaderItem(name = "Pablo", surname = "Escobar", score = 27, index = 3),
        )
    )
}