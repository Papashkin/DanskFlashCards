package com.antsfamily.danskflashcards.ui.leaderboard.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.core.model.Avatar
import com.antsfamily.danskflashcards.core.model.UserItem
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun Podium(
    modifier: Modifier = Modifier,
    first: UserItem,
    second: UserItem,
    third: UserItem
) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(Padding.medium)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(y = (15).dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PodiumAvatarIcon(item = second)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = second.username,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
            )
            Text(
                text = second.score.toString(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-10).dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PodiumAvatarIcon(item = first)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = first.username,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = first.score.toString(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(y = (25).dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PodiumAvatarIcon(item = third)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = third.username,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = third.score.toString(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(12.dp).align(Alignment.BottomCenter))
    }

}

@Preview(showBackground = true)
@Composable
private fun PodiumPreview() {
    Podium(
        first = UserItem(
            id = "MockId1",
            username = "GorillaZ",
            score = 44,
            index = 0,
            isCurrentUser = false,
            avatar = Avatar.DOLLY
        ),
        second = UserItem(
            id = "MockId1",
            username = "Tanz9064",
            score = 32,
            index = 1,
            isCurrentUser = false,
            avatar = Avatar.BEAR
        ),
        third = UserItem(
            id = "MockId1",
            username = "CptMarvel",
            score = 29,
            index = 2,
            isCurrentUser = true,
            avatar = Avatar.SPIRIT
        )
    )
}