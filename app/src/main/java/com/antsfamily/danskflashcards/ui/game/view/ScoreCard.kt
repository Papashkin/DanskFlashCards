package com.antsfamily.danskflashcards.ui.game.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun ScoreCard(
    modifier: Modifier = Modifier,
    titleText: String,
    score: Int
    ) {
    Card(
        modifier = modifier.size(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth().padding(start = Padding.small),
                text = titleText,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = Padding.small),
                text = score.toString(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.displaySmall,
            )
        }
    }
}

@Preview
@Composable
private fun ScoreCardPreview() {
    ScoreCard(
        modifier = Modifier
            .width(100.dp)
            .padding(top = Padding.small),
        titleText = "Score",
        score = 134
    )
}