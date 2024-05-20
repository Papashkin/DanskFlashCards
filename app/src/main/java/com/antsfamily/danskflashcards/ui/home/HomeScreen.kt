package com.antsfamily.danskflashcards.ui.home

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
import com.antsfamily.danskflashcards.ui.home.view.FullScreenLoading
import com.antsfamily.danskflashcards.ui.home.view.GameOverDialog
import com.antsfamily.danskflashcards.ui.home.view.HomeScreenContent
import com.antsfamily.danskflashcards.ui.home.view.StartButton
import com.antsfamily.danskflashcards.ui.home.view.StartedTimer
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsState()
    val dialogData = viewModel.dialogData.collectAsState()
    when (val stateValue = state.value) {
        HomeUiState.Loading -> FullScreenLoading()
        is HomeUiState.Content -> HomeScreenPostsContent(
            stateValue,
            onDanishWordClick = { viewModel.onDanishWordCardClick(it) },
            onEnglishWordClick = { viewModel.onEnglishWordCardClick(it) },
            onStartClick = { viewModel.onStartClick() }
        )

        is HomeUiState.Error -> {
            //TODO add error handler
        }
    }

    dialogData.value?.let {
        GameOverDialog(data = it, viewModel::hideDialog)
    }
}

@Composable
fun HomeScreenPostsContent(
    content: HomeUiState.Content,
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
                HomeScreenContent(content, onDanishWordClick, onEnglishWordClick)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenContent1Preview() {
    HomeScreenPostsContent(
        HomeUiState.Content(
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
fun HomeScreenContent2Preview() {
    HomeScreenPostsContent(
        HomeUiState.Content(
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