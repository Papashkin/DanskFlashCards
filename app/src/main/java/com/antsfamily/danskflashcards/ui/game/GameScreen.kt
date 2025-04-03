package com.antsfamily.danskflashcards.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.danskflashcards.core.presentation.ErrorViewWithRetry
import com.antsfamily.danskflashcards.core.presentation.FullScreenLoading
import com.antsfamily.danskflashcards.ui.game.model.GameOverItem
import com.antsfamily.danskflashcards.ui.game.model.GameStatus
import com.antsfamily.danskflashcards.ui.game.model.TimerItem
import com.antsfamily.danskflashcards.ui.game.model.WordItem
import com.antsfamily.danskflashcards.ui.game.view.GameOverDialog
import com.antsfamily.danskflashcards.ui.game.view.GameScreenContent
import com.antsfamily.danskflashcards.ui.game.view.GameTimer
import com.antsfamily.danskflashcards.ui.game.view.StartAnimationPreloader
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.SetSystemBarColors

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
    SetSystemBarColors(MaterialTheme.colorScheme.background, false)

    val (isTimeUpAnimationVisible, setIsTimeUpAnimationVisible) = remember {
        mutableStateOf(false)
    }
    val (isGameOverDialogVisible, setIsGameOverDialogVisible) = remember { mutableStateOf(false) }
    val (gameOverItem, setGameOverItem) = remember { mutableStateOf(GameOverItem()) }

    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startAnimationFlow.collect {
            setIsTimeUpAnimationVisible(true)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.navigateBackFlow.collect {
            navigateBack()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.gameOverFlow.collect {
            setIsGameOverDialogVisible(true)
            setGameOverItem(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.closeGameOverDialogFlow.collect {
            setIsGameOverDialogVisible(false)
            setGameOverItem(GameOverItem())
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
            isTimeUpAnimationVisible = isTimeUpAnimationVisible,
            onAnimationEnd = { setIsTimeUpAnimationVisible(false) },
            onLearningWordClick = { viewModel.onLearningWordClick(it) },
            onPrimaryWordClick = { viewModel.onPrimaryWordClick(it) }
        )

        is GameUiState.Error -> ErrorViewWithRetry(errorType = stateValue.type) {
            viewModel.onRetryClick()
        }
    }

    if (isGameOverDialogVisible) {
        GameOverDialog(
            item = gameOverItem,
            onExitClick = {
                viewModel.onGameOverDialogClose()
                setIsGameOverDialogVisible(false)
                          },
            onPlayAgainClick = { viewModel.onPlayAgainClick() }
        )
    }
}

@Composable
fun GameContent(
    content: GameUiState.Content,
    isTimeUpAnimationVisible: Boolean,
    onAnimationEnd: () -> Unit,
    onLearningWordClick: (WordItem) -> Unit,
    onPrimaryWordClick: (WordItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(Padding.small)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        with(content) {
            if (status == GameStatus.STARTED) {
                GameTimer(
                    content.timerItem,
                    isAnimationVisible = isTimeUpAnimationVisible,
                ) {
                    onAnimationEnd()
                }
                GameScreenContent(content, onLearningWordClick, onPrimaryWordClick)
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
            learningWords = WORD_CARDS_DANISH,
            primaryWords = WORD_CARDS_ENGLISH,
            timerItem = TimerItem(),
            status = GameStatus.STARTED,
        ),
        false,
        {},
        {},
        {}
    )
}