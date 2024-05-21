package com.antsfamily.danskflashcards.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.danskflashcards.data.GameStatus
import com.antsfamily.danskflashcards.data.WordModel
import com.antsfamily.danskflashcards.ui.game.view.FullScreenLoading
import com.antsfamily.danskflashcards.ui.game.view.GameOverDialog
import com.antsfamily.danskflashcards.ui.game.view.GameScreenContent
import com.antsfamily.danskflashcards.ui.game.view.StartButton
import com.antsfamily.danskflashcards.ui.game.view.StartedTimer
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun GameScreen(viewModel: GameViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsState()
    val dialogData = viewModel.dialogData.collectAsState()
    when (val stateValue = state.value) {
        GameUiState.Loading -> FullScreenLoading()
        is GameUiState.Content -> GameScreenPostsContent(
            stateValue,
            onDanishWordClick = { viewModel.onDanishWordCardClick(it) },
            onEnglishWordClick = { viewModel.onEnglishWordCardClick(it) },
            onStartClick = { viewModel.onStartClick() }
        )

        is GameUiState.Error -> {
            //TODO add error handler
        }
    }

    dialogData.value?.let {
        GameOverDialog(data = it, viewModel::hideDialog)
    }
}

@Composable
fun GameScreenPostsContent(
    content: GameUiState.Content,
    onDanishWordClick: (WordModel) -> Unit,
    onEnglishWordClick: (WordModel) -> Unit,
    onStartClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = Padding.large, vertical = Padding.medium)
            .fillMaxSize(),
    ) {
        with(content) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                when (status) {
                    GameStatus.STARTED -> StartedTimer(remainingCountdownTime, timerProgress)
                    else -> StartButton(onClick = onStartClick)
                }
            }
            if (status == GameStatus.STARTED) {
                GameScreenContent(content, onDanishWordClick, onEnglishWordClick)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameScreenContent1Preview() {
    GameScreenPostsContent(
        GameUiState.Content(
            danish = listOf(),
            english = listOf(),
            1000,
            500,
            status = GameStatus.READY,
        ),
        {},
        {},
        {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameScreenContent2Preview() {
    GameScreenPostsContent(
        GameUiState.Content(
            danish = listOf(
                WordModel(
                    value = "kolonne",
                    id = 986,
                    isGuessed = false,
                    isSelected = false,
                    isWrong = false
                ),
                WordModel(
                    value = "molekyle",
                    id = 987,
                    isGuessed = false,
                    isSelected = true,
                    isWrong = false
                ),
                WordModel(
                    value = "vælg",
                    id = 988,
                    isGuessed = true,
                    isSelected = true,
                    isWrong = false
                ),
                WordModel(
                    value = "forkert",
                    id = 989,
                    isGuessed = false,
                    isSelected = false,
                    isWrong = false
                ),
                WordModel(
                    value = "grå",
                    id = 990,
                    isGuessed = false,
                    isSelected = false,
                    isWrong = true
                ),
            ),
            english = listOf(
                WordModel(
                    value = "column",
                    id = 991,
                    isGuessed = false,
                    isSelected = true,
                    isWrong = false
                ),
                WordModel(
                    value = "molecule",
                    id = 992,
                    isGuessed = true,
                    isSelected = false,
                    isWrong = false
                ),
                WordModel(
                    value = "select",
                    id = 993,
                    isGuessed = false,
                    isSelected = false,
                    isWrong = false
                ),
                WordModel(
                    value = "wrong",
                    id = 994,
                    isGuessed = false,
                    isSelected = true,
                    isWrong = false
                ),
                WordModel(
                    value = "gray",
                    id = 996,
                    isGuessed = false,
                    isSelected = false,
                    isWrong = false
                ),
            ),
            1000,
            500,
            status = GameStatus.STARTED,
        ),
        {},
        {},
        {}
    )
}