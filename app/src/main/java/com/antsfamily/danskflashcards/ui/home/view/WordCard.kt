package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.data.GameStatus
import com.antsfamily.danskflashcards.data.WordModel
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.alert
import com.antsfamily.danskflashcards.ui.theme.wistful_0
import com.antsfamily.danskflashcards.ui.theme.wistful_100
import com.antsfamily.danskflashcards.ui.theme.wistful_1000
import com.antsfamily.danskflashcards.ui.theme.wistful_200
import com.antsfamily.danskflashcards.ui.theme.wistful_300
import com.antsfamily.danskflashcards.ui.theme.wistful_600

@Composable
fun WordCard(status: GameStatus, word: WordModel, onClick: (WordModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = word.isSelected,
                enabled = status == GameStatus.STARTED && !word.isGuessed,
                role = null,
                onClick = {
                    onClick(word)
                }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor =  when {
                word.isSelected && !word.isGuessed -> wistful_600
                word.isGuessed -> wistful_100
                word.isWrong -> alert
                else -> wistful_300
            },
            contentColor = wistful_1000,
            disabledContainerColor = wistful_100,
            disabledContentColor = wistful_200
        ),
    ) {
        Box(
            modifier = Modifier.padding(vertical = Padding.medium)
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = Padding.medium)
                    .fillMaxWidth(),
                text = word.value,
                fontSize = FontSize.H6,
                fontWeight = FontWeight.SemiBold,
                color =  when {
                    word.isSelected && !word.isGuessed ->  wistful_0
                    word.isGuessed -> wistful_300
                    word.isWrong -> wistful_0
                    else -> wistful_1000
                },
                textAlign = TextAlign.Center,
                style = if (word.isGuessed) {
                    TextStyle(textDecoration = TextDecoration.LineThrough)
                } else LocalTextStyle.current
            )
        }
    }
}

@Preview
@Composable
fun startedWordCard() {
    WordCard(
        status = GameStatus.STARTED,
        word = WordModel(1, "mock", false, false, false),
        onClick = {}
    )
}

@Preview
@Composable
fun selectedWordCard() {
    WordCard(
        status = GameStatus.FINISHED,
        word = WordModel(1, "mock", true, false, false),
        onClick = {}
    )
}

@Preview
@Composable
fun startedWrongWordCard() {
    WordCard(
        status = GameStatus.STARTED,
        word = WordModel(1, "mock", false, false, true),
        onClick = {}
    )
}

@Preview
@Composable
fun finishedRightWordCard() {
    WordCard(
        status = GameStatus.STARTED,
        word = WordModel(1, "mock", false, true, false),
        onClick = {}
    )
}
