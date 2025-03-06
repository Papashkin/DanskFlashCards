package com.antsfamily.danskflashcards.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
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

        is GameUiState.Content -> GameContent(
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