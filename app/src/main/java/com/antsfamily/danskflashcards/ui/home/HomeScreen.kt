package com.antsfamily.danskflashcards.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.data.WordModel
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsState()
    when (val stateValue = state.value) {
        HomeUiState.Loading -> FullScreenLoading()
        is HomeUiState.Content -> HomeScreenPostsContent(
            stateValue,
            onDanishWordClick = { viewModel.onDanishWordCardClick(it) },
            onEnglishWordClick = { viewModel.onEnglishWordCardClick(it) },
        )

        is HomeUiState.Error -> {
            //TODO add error handler
        }
    }
}

@Composable
fun HomeScreenPostsContent(
    content: HomeUiState.Content,
    onDanishWordClick: (WordModel) -> Unit,
    onEnglishWordClick: (WordModel) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = Padding.xLarge, vertical = Padding.medium)) {
        CountdownProgressBar(
            modifier = Modifier.padding(top = Padding.medium),
            totalTimeSec = content.totalCountdownTime,
            remainingTimeSec = content.remainingCountdownTime
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Padding.medium)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = Padding.small)
                ) {
                    GridWithCards(content.danish) { onDanishWordClick(it) }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = Padding.small)
                ) {
                    GridWithCards(content.english) { onEnglishWordClick(it) }
                }
            }
        }
    }
}

@Composable
fun GridWithCards(words: List<WordModel>, onClick: (WordModel) -> Unit) {
    LazyColumn {
        items(words.size) {
            WordCard(words[it], onClick)
        }
    }
}

@Composable
fun FullScreenLoading() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenContentPreview() {
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
        ),
        {},
        {}
    )
}