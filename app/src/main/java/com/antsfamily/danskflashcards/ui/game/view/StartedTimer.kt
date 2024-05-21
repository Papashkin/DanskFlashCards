package com.antsfamily.danskflashcards.ui.game.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.wistful_100
import com.antsfamily.danskflashcards.ui.theme.wistful_700
import com.antsfamily.danskflashcards.util.toTimeFormat

@Composable
fun StartedTimer(remainingTimeSec: Long, progress: Float) {
    Box(
        modifier = Modifier
            .height(48.dp)
            .background(
                color = wistful_100,
                RoundedCornerShape(Padding.large)
            )
    ) {
        Row(
            Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = Padding.medium)
        ) {
            TimerText(remainingTimeSec)
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Padding.small, end = Padding.xSmall)
                    .align(Alignment.CenterVertically)
                    .height(12.dp),
                strokeCap = StrokeCap.Round,
                color = wistful_700,
                trackColor = MaterialTheme.colorScheme.background
            )
        }
    }
}


@Composable
fun TimerText(timeInSec: Long, modifier: Modifier = Modifier) {
    Text(
        text = timeInSec.toTimeFormat(),
        modifier = modifier.background(Color.Transparent),
        color = wistful_700,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Preview
@Composable
fun StartedTimerPreview() {
    StartedTimer(remainingTimeSec = 52L, 0.5f)
}