package com.antsfamily.danskflashcards.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.ui.game.model.GameOverItem
import com.antsfamily.danskflashcards.ui.game.model.GameStatus
import com.antsfamily.danskflashcards.ui.game.model.TimerItem
import com.antsfamily.danskflashcards.ui.game.model.WordItem
import com.antsfamily.danskflashcards.core.presentation.FullScreenLoading
import com.antsfamily.danskflashcards.ui.game.view.GameOverDialog
import com.antsfamily.danskflashcards.ui.game.view.GameScreenContent
import com.antsfamily.danskflashcards.ui.game.view.GameTimer
import com.antsfamily.danskflashcards.ui.game.view.StartAnimationPreloader
import com.antsfamily.danskflashcards.core.presentation.ErrorViewWithRetry
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.SetSystemBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    userId: String,
    username: String,
    score: Int,
    viewModel: GameViewModel = hiltViewModel<GameViewModel, GameViewModel.Factory> {
        it.create(userId = userId, username = username, score = score)
    },
    navigateBack: () -> Unit,
) {
    var isTimeUpAnimationVisible by remember { mutableStateOf(false) }
    var bottomSheetData by remember { mutableStateOf<GameOverItem?>(null) }

    SetSystemBarColors(MaterialTheme.colorScheme.background, false)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startAnimationFlow.collect {
            isTimeUpAnimationVisible = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.gameOverFlow.collect {
            bottomSheetData = it
            if (!sheetState.isVisible) {
                sheetState.show()
            }
        }
    }

    when (val stateValue = state.value) {
        is GameUiState.Countdown ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                StartAnimationPreloader { viewModel.onAnimationFinished() }
            }

        is GameUiState.Loading -> FullScreenLoading()
        is GameUiState.Content -> GameContent(
            stateValue,
            isTimeUpAnimationVisible,
            onAnimationEnd = { isTimeUpAnimationVisible = false },
            onDanishWordClick = { viewModel.onDanishWordCardClick(it) },
            onEnglishWordClick = { viewModel.onEnglishWordCardClick(it) }
        )

        is GameUiState.Error -> ErrorViewWithRetry(errorType = stateValue.type) {
            viewModel.onRetryClick()
        }
    }

    bottomSheetData?.let {
        GameOverDialog(data = it, sheetState) {
            viewModel.onGameOverDialogClose()
            bottomSheetData = null
            navigateBack()
        }
    }
}

@Composable
fun GameContent(
    content: GameUiState.Content,
    isTimeUpAnimationVisible: Boolean,
    onAnimationEnd: () -> Unit,
    onDanishWordClick: (WordItem) -> Unit,
    onEnglishWordClick: (WordItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(Padding.small)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        with(content) {
            if (status == GameStatus.STARTED) {
                GameTimer(content.timerItem, isAnimationVisible = isTimeUpAnimationVisible) {
                    onAnimationEnd()
                }
                GameScreenContent(content, onDanishWordClick, onEnglishWordClick)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameScreenLoadingPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        StartAnimationPreloader { }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameScreenContentPreview() {
    val WORD_CARDS_DANISH = listOf(
        WordItem(value = "kolonne", id = 986, isGuessed = false, isSelected = false, isWrong = false),
        WordItem(value = "molekyle", id = 987, isGuessed = false, isSelected = true, isWrong = false),
        WordItem(value = "vælg", id = 988, isGuessed = true, isSelected = true, isWrong = false),
        WordItem(value = "forkert", id = 989, isGuessed = false, isSelected = false, isWrong = false),
        WordItem(value = "grå", id = 990, isGuessed = false, isSelected = false, isWrong = true),
        WordItem(value = "grå", id = 990, isGuessed = false, isSelected = false, isWrong = true)
    )

    val WORD_CARDS_ENGLISH = listOf(
        WordItem(value = "column", id = 991, isGuessed = false, isSelected = true, isWrong = false),
        WordItem(value = "molecule", id = 992, isGuessed = true, isSelected = false, isWrong = false),
        WordItem(value = "select", id = 993, isGuessed = false, isSelected = false, isWrong = false),
        WordItem(value = "wrong", id = 994, isGuessed = false, isSelected = true, isWrong = false),
        WordItem(value = "gray", id = 996, isGuessed = false, isSelected = false, isWrong = false),
        WordItem(value = "gray", id = 996, isGuessed = false, isSelected = false, isWrong = false),
    )

    GameContent(
        GameUiState.Content(
            danish = WORD_CARDS_DANISH,
            english = WORD_CARDS_ENGLISH,
            timerItem = TimerItem(),
            status = GameStatus.STARTED,
        ),
        false,
        {},
        {},
        {}
    )
}