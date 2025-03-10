package com.antsfamily.danskflashcards.ui.game.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.antsfamily.danskflashcards.ui.game.GameUiState
import com.antsfamily.danskflashcards.ui.game.model.GameStatus
import com.antsfamily.danskflashcards.ui.game.model.TimerModel
import com.antsfamily.danskflashcards.ui.game.model.WORD_CARDS_DANISH
import com.antsfamily.danskflashcards.ui.game.model.WORD_CARDS_ENGLISH
import com.antsfamily.danskflashcards.ui.game.model.WordModel
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun GameScreenContent(
    content: GameUiState.Content,
    onDanishWordClick: (WordModel) -> Unit,
    onEnglishWordClick: (WordModel) -> Unit,
) {
    Box(modifier = Modifier.padding(vertical = Padding.large)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Padding.small),
            modifier = Modifier.align(Alignment.Center)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(Padding.small),
                verticalArrangement = Arrangement.spacedBy(Padding.small)
            ) {
                items(content.danish.size) {
                    WordCard(content.status, content.danish[it]) { model ->
                        onDanishWordClick(model)
                    }
                    Spacer(modifier = Modifier.height(Padding.small))
                }
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(Padding.small),
                verticalArrangement = Arrangement.spacedBy(Padding.small)
            ) {
                items(content.english.size) {
                    WordCard(content.status, content.english[it]) { model ->
                        onEnglishWordClick(model)
                    }
                    Spacer(modifier = Modifier.height(Padding.small))
                }
            }
        }
    }
}

@Preview
@Composable
fun GameScreenContentPreview(modifier: Modifier = Modifier) {
    GameScreenContent(
        content = GameUiState.Content(
            danish = WORD_CARDS_DANISH,
            english = WORD_CARDS_ENGLISH,
            TimerModel(),
            status = GameStatus.STARTED,
        ),
        onDanishWordClick = {},
        onEnglishWordClick = {}
    )
}