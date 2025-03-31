package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.ui.home.model.LeaderItem
import com.antsfamily.danskflashcards.ui.home.model.mapToTextColor
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.grey_200
import com.antsfamily.danskflashcards.ui.theme.grey_500
import com.antsfamily.danskflashcards.ui.theme.light_accent
import com.antsfamily.danskflashcards.ui.theme.wistful_0
import com.antsfamily.danskflashcards.ui.theme.wistful_700

@Composable
fun LeaderboardCard(
    modifier: Modifier = Modifier,
    item: LeaderItem,
    isUser: Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LeaderboardIcon(modifier, item = item)
                Row(modifier.fillMaxWidth()) {
                    Text(
                        modifier = modifier.weight(5f),
                        text = item.modifiedName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start,
                        color = if (isUser) light_accent else item.mapToTextColor()
                    )

                    Text(
                        modifier = modifier.weight(1f),
                        text = item.score.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = if (isUser) light_accent else item.mapToTextColor()
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Padding.small),
                thickness = 1.dp,
                color = grey_200
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LeaderboardItemPreview(modifier: Modifier = Modifier) {
    Column {
        HorizontalDivider(thickness = 20.dp)
        LeaderboardCard(item = LeaderItem(name = "John", surname = "Doe", score = 25, index = 0), isUser = false)
        LeaderboardCard(item = LeaderItem(name = "John", surname = "   ", score = 25, index = 1), isUser = false)
        LeaderboardCard(item = LeaderItem(name = "John", surname = "", score = 25, index = 2), isUser = false)
        LeaderboardCard(item = LeaderItem(name = "John", surname = null, score = 25, index = 7), isUser = true)
    }
}