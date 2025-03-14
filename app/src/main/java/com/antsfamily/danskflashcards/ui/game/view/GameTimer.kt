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
import com.antsfamily.danskflashcards.ui.game.model.TimerItem
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.alert
import com.antsfamily.danskflashcards.ui.theme.wistful_700

@Composable
fun GameTimer(model: TimerItem) {
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
            color = if (model.isLastResort) alert else wistful_700,
            fontSize = if (model.isLastResort) FontSize.H3 else FontSize.H4,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun GameTimerPreview1() {
    GameTimer(TimerItem(remainTime = 75, progress = 0.4f))
}

@Preview
@Composable
fun GameTimerPreview2() {
    GameTimer(TimerItem(remainTime = 4, progress = 0.4f))
}