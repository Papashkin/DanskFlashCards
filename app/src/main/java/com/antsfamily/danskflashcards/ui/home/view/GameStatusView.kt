package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.antsfamily.danskflashcards.data.GameStatus
import com.antsfamily.danskflashcards.ui.home.HomeUiState
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun GameStatusView(content: HomeUiState.Content, onClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = Padding.xLarge, vertical = Padding.medium)) {
        TestCountdownTimer(
            modifier = Modifier.padding(top = Padding.medium),
            timerProgress = content.timerProgress,
            remainingTimeSec = content.remainingCountdownTime,
            status = content.status,
            onClick = onClick
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameStatusViewPreview1() {
    GameStatusView(HomeUiState.Content(status = GameStatus.STARTED)) {}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameStatusViewPreview2() {
    GameStatusView(HomeUiState.Content(status = GameStatus.READY)) {}
}