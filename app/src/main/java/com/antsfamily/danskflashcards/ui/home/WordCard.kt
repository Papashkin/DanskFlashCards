package com.antsfamily.danskflashcards.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.data.GameStatus
import com.antsfamily.danskflashcards.data.WordModel
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun WordCard(status: GameStatus, word: WordModel, onClick: (WordModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = status == GameStatus.STARTED && !word.isGuessed) {
                onClick(word)
            }
            .border(
                2.dp,
                when {
                    word.isSelected && !word.isGuessed -> Color.DarkGray
                    word.isGuessed -> Color.Green
                    word.isWrong -> Color.Red
                    else -> Color.LightGray
                },
                RoundedCornerShape(Padding.medium)
            ),
    ) {
        Box(
            modifier = Modifier.padding(vertical = Padding.medium)
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = Padding.xLarge)
                    .fillMaxWidth(),
                text = word.value,
                fontSize = FontSize.H6,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
    Spacer(modifier = Modifier.height(Padding.large))
}