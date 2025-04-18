package com.antsfamily.danskflashcards.ui.game.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.antsfamily.danskflashcards.ui.game.model.WordItem
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun GameScreenContent(
    learningWords: List<WordItem>,
    primaryWords: List<WordItem>,
    onLearningWordClick: (WordItem) -> Unit,
    onPrimaryWordClick: (WordItem) -> Unit,
) {
    Box {
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
                items(learningWords) { word ->
                    WordCard(word) { onLearningWordClick(it) }
                    Spacer(modifier = Modifier.height(Padding.small))
                }
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(Padding.small),
                verticalArrangement = Arrangement.spacedBy(Padding.small)
            ) {
                items(primaryWords) { word ->
                    WordCard(word) { onPrimaryWordClick(it) }
                    Spacer(modifier = Modifier.height(Padding.small))
                }
            }
        }
    }
}

@Preview
@Composable
fun GameScreenContentPreview(modifier: Modifier = Modifier) {
    val WORD_CARDS_DANISH = listOf(
        WordItem(value = "kolonne", id = 986, isGuessed = false, isSelected = false, isWrong = false),
        WordItem(value = "molekyle", id = 987, isGuessed = false, isSelected = true, isWrong = false),
        WordItem(value = "vælg", id = 988, isGuessed = true, isSelected = true, isWrong = false),
        WordItem(value = "forkert", id = 989, isGuessed = false, isSelected = false, isWrong = false),
        WordItem(value = "grå", id = 990, isGuessed = false, isSelected = false, isWrong = true),
    )

    val WORD_CARDS_ENGLISH = listOf(
        WordItem(value = "column", id = 991, isGuessed = false, isSelected = true, isWrong = false),
        WordItem(value = "molecule", id = 992, isGuessed = true, isSelected = false, isWrong = false),
        WordItem(value = "select", id = 993, isGuessed = false, isSelected = false, isWrong = false),
        WordItem(value = "wrong", id = 994, isGuessed = false, isSelected = true, isWrong = false),
        WordItem(value = "gray", id = 996, isGuessed = false, isSelected = false, isWrong = false),
    )

    GameScreenContent(
        learningWords = WORD_CARDS_DANISH,
        primaryWords = WORD_CARDS_ENGLISH,
        onLearningWordClick = {},
        onPrimaryWordClick = {}
    )
}