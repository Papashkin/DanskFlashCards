package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.model.Avatar
import com.antsfamily.danskflashcards.ui.home.model.LeaderItem
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.grey_500

@Composable
fun LeaderboardView(
    modifier: Modifier = Modifier,
    items: List<LeaderItem>,
    onShowAllClick: () -> Unit
) {
    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Padding.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.home_leaderboard_title),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge,
            )
            if (items.isNotEmpty()) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = grey_500
                    ),
                    contentPadding = PaddingValues(0.dp),
                    onClick = { onShowAllClick() }
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = Padding.small),
                        text = stringResource(R.string.home_leaderboard_button_text),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
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
            text = stringResource(R.string.home_leaderboard_is_empty),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = modifier.padding(Padding.large),
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LeaderboardViewPreview0(modifier: Modifier = Modifier) {
    LeaderboardView(items = emptyList()) {}
}

@Preview(showBackground = true)
@Composable
fun LeaderboardViewPreview1(modifier: Modifier = Modifier) {
    LeaderboardView(
        items = listOf(
            LeaderItem(username = "JohnDoe", score = 44, index = 0, isUser = false, avatar = Avatar.DEFAULT),
            LeaderItem(username = "PabloEscobar", score = 32, index = 1, isUser = false, avatar = Avatar.DEFAULT),
            LeaderItem(username = "AndreaCorti", score = 29, index = 2, isUser = true, avatar = Avatar.DEFAULT)
        )
    ) {}
}
