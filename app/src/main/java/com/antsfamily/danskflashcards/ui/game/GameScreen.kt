package com.antsfamily.danskflashcards.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.antsfamily.danskflashcards.ui.game.model.GameOverModel
import com.antsfamily.danskflashcards.ui.game.model.GameStatus
import com.antsfamily.danskflashcards.ui.game.model.TimerModel
import com.antsfamily.danskflashcards.ui.game.model.WORD_CARDS_DANISH
import com.antsfamily.danskflashcards.ui.game.model.WORD_CARDS_ENGLISH
import com.antsfamily.danskflashcards.ui.game.model.WordModel
import com.antsfamily.danskflashcards.ui.game.view.GameOverDialog
import com.antsfamily.danskflashcards.ui.game.view.GameScreenContent
import com.antsfamily.danskflashcards.ui.game.view.GameTimer
import com.antsfamily.danskflashcards.ui.game.view.StartAnimationPreloader
import com.antsfamily.danskflashcards.ui.theme.Padding

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
    var bottomSheetData by remember { mutableStateOf<GameOverModel?>(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.timer_up_new)
    )
    val animationProgress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        isPlaying = isTimeUpAnimationVisible
    )
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

    LaunchedEffect(animationProgress) {
        if (animationProgress == 1.0f) {
            isTimeUpAnimationVisible = false
        }
    }

    if (isTimeUpAnimationVisible) {
        LottieAnimation(
            composition = composition,
            progress = animationProgress,
            modifier = Modifier.size(210.dp)
        )
    }

    when (val stateValue = state.value) {
        GameUiState.Loading ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                StartAnimationPreloader { viewModel.onAnimationFinished() }
            }

        is GameUiState.Content -> GameContent(
            stateValue,
            onDanishWordClick = { viewModel.onDanishWordCardClick(it) },
            onEnglishWordClick = { viewModel.onEnglishWordCardClick(it) }
        )

        is GameUiState.Error -> {
            //TODO add error handler
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
    onDanishWordClick: (WordModel) -> Unit,
    onEnglishWordClick: (WordModel) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(Padding.large)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        with(content) {
            if (status == GameStatus.STARTED) {
                GameTimer(content.timerModel)
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
    GameContent(
        GameUiState.Content(
            danish = WORD_CARDS_DANISH,
            english = WORD_CARDS_ENGLISH,
            timerModel = TimerModel(),
            status = GameStatus.STARTED,
        ),
        {},
        {}
    )
}