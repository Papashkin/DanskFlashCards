package com.antsfamily.danskflashcards.ui.game.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.ui.game.model.TimerModel
import com.antsfamily.danskflashcards.ui.theme.wistful_700
import com.antsfamily.danskflashcards.util.toTimeFormat

@Composable
fun GameTimer(model: TimerModel) {
    Box(
        modifier = Modifier.size(140.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = { model.progress },
            strokeWidth = 8.dp
        )

        Text(
            text = model.remainTimeString,
            color = wistful_700,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun GameTimerPreview() {
    GameTimer(TimerModel(remainTime = 75, progress = 0.4f))
}