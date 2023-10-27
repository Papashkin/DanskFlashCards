package com.antsfamily.danskflashcards.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.danskflashcards.data.GameStatus
import com.antsfamily.danskflashcards.data.WordModel
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
        GameOverDialog(pairsAmount = dialogData.value.pairsAmount) {
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
    val overlayColor = if (content.status == GameStatus.STARTED) {
        MaterialTheme.colorScheme.background.copy(alpha = 0f)
    } else {
        MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
    }

    Column(modifier = Modifier.padding(horizontal = Padding.xLarge, vertical = Padding.medium)) {
        when (content.status) {
            GameStatus.STARTED -> CountdownProgressBar(
                modifier = Modifier.padding(top = Padding.medium),
                totalTimeSec = content.totalCountdownTime,
                remainingTimeSec = content.remainingCountdownTime
            )
            else -> StartButton(modifier = Modifier.padding(top = Padding.medium), onStartClick)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(color = overlayColor, size = size)
                }
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
                    GridWithCards(content.status, content.danish) { onDanishWordClick(it) }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = Padding.small)
                ) {
                    GridWithCards(content.status, content.english) { onEnglishWordClick(it) }
                }
            }
        }
    }
}

@Composable
fun StartButton(modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .border(
                    shape = CircleShape,
                    border = BorderStroke(8.dp, color = MaterialTheme.colorScheme.tertiary)
                )
                .clip(CircleShape)
        ) {
            IconButton(onClick = onClick, modifier = Modifier.padding(40.dp)) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}

@Composable
fun GridWithCards(status: GameStatus, words: List<WordModel>, onClick: (WordModel) -> Unit) {
    LazyColumn {
        items(words.size) {
            WordCard(status, words[it], onClick)
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
            status = GameStatus.READY,
        ),
        {},
        {},
        {}
    )
}