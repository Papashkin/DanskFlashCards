package com.antsfamily.danskflashcards.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.antsfamily.danskflashcards.ui.home.view.TestCountdownTimer
import com.antsfamily.danskflashcards.ui.home.view.WordCard
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

    if (dialogData.value.isVisible) {
        GameOverDialog(data = dialogData.value) {
            viewModel.hideDialog()
        }
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
            .padding(horizontal = Padding.large, vertical = Padding.xSmall)
            .fillMaxSize(),
    ) {
        TestCountdownTimer(
            modifier = Modifier.padding(vertical = Padding.medium),
            remainingTimeSec = content.remainingCountdownTime,
            timerProgress = content.timerProgress,
            status = content.status,
            onClick = onStartClick
        )

        if (content.status == GameStatus.STARTED) {
            Box(modifier = Modifier.fillMaxSize()) {
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
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenContent1Preview() {
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