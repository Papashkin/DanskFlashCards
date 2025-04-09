package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.core.model.Avatar
import com.antsfamily.danskflashcards.core.model.toIconRes
import com.antsfamily.danskflashcards.core.util.mapToColor
import com.antsfamily.danskflashcards.core.util.mapToTextColor
import com.antsfamily.danskflashcards.ui.home.model.LeaderItem
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.light_accent

@Composable
fun LeaderboardCard(
    modifier: Modifier = Modifier,
    item: LeaderItem,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = modifier.padding(vertical = Padding.small),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(horizontal = Padding.small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .width(48.dp)
                        .padding(horizontal = Padding.small),
                    text = item.place.toString(),
                    color = item.mapToColor(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Image(
                    imageVector = ImageVector.vectorResource(item.avatar.toIconRes()),
                    contentDescription = null,
                )
                Row {
                    Text(
                        modifier = modifier
                            .padding(start = Padding.medium)
                            .weight(5f),
                        text = item.username,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start,
                        color = if (item.isUser) light_accent else item.mapToTextColor()
                    )
                    Text(
                        modifier = modifier.weight(1f),
                        text = item.score.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = if (item.isUser) light_accent else item.mapToTextColor()
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(start = Padding.medium, top = Padding.medium)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LeaderboardItemPreview(modifier: Modifier = Modifier) {
    Column(modifier) {
        LeaderboardCard(
            item = LeaderItem(
                username = "John",
                score = 25,
                index = 1,
                isUser = false,
                avatar = Avatar.PEN
            )
        )
        LeaderboardCard(
            item = LeaderItem(
                username = "John",
                score = 25,
                index = 777,
                isUser = false,
                avatar = Avatar.PEN
            )
        )
        LeaderboardCard(
            item = LeaderItem(
                username = "John",
                score = 999,
                index = 7,
                isUser = true,
                avatar = Avatar.PEN
            )
        )
    }
}