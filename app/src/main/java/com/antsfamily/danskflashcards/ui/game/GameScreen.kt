package com.antsfamily.danskflashcards.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.antsfamily.danskflashcards.data.GameStatus
import com.antsfamily.danskflashcards.data.WordModel
import com.antsfamily.danskflashcards.ui.game.view.GameOverDialog
import com.antsfamily.danskflashcards.ui.game.view.GameScreenContent
import com.antsfamily.danskflashcards.ui.game.view.StartAnimationPreloader
import com.antsfamily.danskflashcards.ui.game.view.StartedTimer
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val dialogData = viewModel.dialogData.collectAsState()
    when (val stateValue = state.value) {
        GameUiState.Loading ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                StartAnimationPreloader { viewModel.onAnimationFinished() }
            }

        is GameUiState.Content -> Content(
            stateValue,
            onDanishWordClick = { viewModel.onDanishWordCardClick(it) },
            onEnglishWordClick = { viewModel.onEnglishWordCardClick(it) }
        )

        is GameUiState.Error -> {
            //TODO add error handler
        }
    }

    dialogData.value?.let {
        GameOverDialog(data = it) {
            viewModel.hideDialog()
            navController.popBackStack()
        }
    }
}

@Composable
fun Content(
    content: GameUiState.Content,
    onDanishWordClick: (WordModel) -> Unit,
    onEnglishWordClick: (WordModel) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = Padding.large, vertical = Padding.medium)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround // .SpaceBetween
    ) {
        with(content) {
            if (status == GameStatus.STARTED) {
                StartedTimer(remainingCountdownTime, timerProgress)
                GameScreenContent(content, onDanishWordClick, onEnglishWordClick)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameScreenLoadingPreview() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        StartAnimationPreloader { }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameScreenContentPreview() {
    Content(
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
        {}
    )
}