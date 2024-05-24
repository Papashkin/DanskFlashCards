package com.antsfamily.danskflashcards.ui.game.view

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.antsfamily.danskflashcards.R

@Composable
fun StartAnimationPreloader(
    modifier: Modifier = Modifier,
    onAnimationFinish: () -> Unit
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.start_countdown)
    )

    val animationProgress by animateLottieCompositionAsState(composition)

    LottieAnimation(
        composition = composition,
        modifier = modifier.size(100.dp)
    )

    if (animationProgress == 1.0f) {
        onAnimationFinish()
    }
}

@Preview(showBackground = true)
@Composable
private fun StartAnimationPreloaderPreview() {
    StartAnimationPreloader {}
}