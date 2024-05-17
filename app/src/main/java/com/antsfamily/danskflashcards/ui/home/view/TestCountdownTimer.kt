package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.antsfamily.danskflashcards.data.GameStatus
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.wistful_0
import com.antsfamily.danskflashcards.ui.theme.wistful_100
import com.antsfamily.danskflashcards.ui.theme.wistful_700
import com.antsfamily.danskflashcards.util.toTimeFormat

@Composable
fun TestCountdownTimer(
    modifier: Modifier,
    status: GameStatus,
    remainingTimeSec: Long,
    timerProgress: Float,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (status) {
                GameStatus.STARTED ->
                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .zIndex(1f)
                            .offset(x = 12.dp)
                            .background(
                                color = wistful_100,
                                RoundedCornerShape(Padding.large)
                            )
                    ) {

                        TimerText(
                            remainingTimeSec,
                            Modifier
                                .align(Alignment.CenterStart)
                                .padding(horizontal = Padding.medium)
                        )
                    }
                else -> Button(
                    onClick = onClick,
                    colors = buttonColors(
                        containerColor = wistful_700,
                        contentColor = wistful_0
                    ),
                    shape = RoundedCornerShape(Padding.large),
                    modifier = Modifier.height(48.dp).fillMaxWidth().padding(horizontal = Padding.medium)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = Padding.medium),
                        text = "start",
                        color = wistful_0,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

        if (status == GameStatus.STARTED) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = (-8).dp)
                    .height(48.dp)
                    .background(
                        color = wistful_100,
                        RoundedCornerShape(topEnd = Padding.xLarge, bottomEnd = Padding.xLarge)
                    )
            ) {
                LinearProgressIndicator(
                    progress = timerProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Padding.medium, end = Padding.medium)
                        .height(12.dp),
                    strokeCap = StrokeCap.Round,
                    color = wistful_700,
                    trackColor = MaterialTheme.colorScheme.background
                )
            }
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TestCountdownTimerPreview1() {
    TestCountdownTimer(modifier = Modifier, GameStatus.READY, 120, 0.87f) {}
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TestCountdownTimerPreview2() {
    TestCountdownTimer(modifier = Modifier, GameStatus.STARTED, 120, 0.47f) {}
}
