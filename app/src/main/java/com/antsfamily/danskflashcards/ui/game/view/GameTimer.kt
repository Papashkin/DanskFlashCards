package com.antsfamily.danskflashcards.ui.game.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.ui.game.model.TimerItem
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.alert
import com.antsfamily.danskflashcards.ui.theme.wistful_700

@Composable
fun GameTimer(
    modifier: Modifier = Modifier,
    timer: TimerItem,
    isAnimationVisible: Boolean,
    currentScore: Int,
    personalBest: Int,
    onAnimationEnd: () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.timer_up_new)
    )
    val animationProgress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        isPlaying = isAnimationVisible
    )
    val pulsarScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulsar"
    )

    LaunchedEffect(animationProgress) {
        if (animationProgress == 1.0f) {
            onAnimationEnd()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(160.dp),
                progress = { timer.progress },
                strokeWidth = 8.dp
            )

            Text(
                modifier = Modifier
                    .scale(if (timer.isLastResort) pulsarScale else 1f),
                text = timer.remainTimeString,
                color = if (timer.isLastResort) alert else wistful_700,
                fontSize = if (timer.isLastResort) FontSize.H2 else FontSize.H3,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )

            if (isAnimationVisible) {
                LottieAnimation(
                    composition = composition,
                    progress = animationProgress,
                    modifier = Modifier
                        .size(160.dp)
                        .align(Alignment.Center)
                        .padding(bottom = 36.dp)
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(Padding.small)) {
            ScoreCard(
                modifier = Modifier.weight(1f),
                score = currentScore,
                titleText = stringResource(R.string.score_card_title_score)
            )
            ScoreCard(
                modifier = Modifier.weight(1f),
                score = personalBest,
                titleText = stringResource(R.string.score_card_title_record)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameTimerPreview1() {
    GameTimer(
        timer = TimerItem(remainTime = 75, progress = 0.4f),
        isAnimationVisible = false,
        currentScore = 13,
        personalBest = 65
    ) {}
}

@Preview(showBackground = true)
@Composable
fun GameTimerPreview2() {
    GameTimer(
        timer = TimerItem(remainTime = 4, progress = 0.4f),
        isAnimationVisible = true,
        currentScore = 13,
        personalBest = 65
    ) {}
}