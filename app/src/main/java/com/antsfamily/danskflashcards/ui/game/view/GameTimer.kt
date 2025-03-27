package com.antsfamily.danskflashcards.ui.game.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.antsfamily.danskflashcards.ui.theme.alert
import com.antsfamily.danskflashcards.ui.theme.wistful_700

@Composable
fun GameTimer(
    model: TimerItem,
    isAnimationVisible: Boolean,
    onAnimationEnd: () -> Unit,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.timer_up_new)
    )
    val animationProgress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        isPlaying = isAnimationVisible
    )

    LaunchedEffect(animationProgress) {
        if (animationProgress == 1.0f) {
            onAnimationEnd()
        }
    }

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

        if (isAnimationVisible) {
            LottieAnimation(
                composition = composition,
                progress = animationProgress,
                modifier = Modifier.size(140.dp)
                    .align(Alignment.Center)
                    .padding(bottom = 36.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameTimerPreview1() {
    GameTimer(TimerItem(remainTime = 75, progress = 0.4f), false) {}
}

@Preview(showBackground = true)
@Composable
fun GameTimerPreview2() {
    GameTimer(TimerItem(remainTime = 4, progress = 0.4f), true) {}
}