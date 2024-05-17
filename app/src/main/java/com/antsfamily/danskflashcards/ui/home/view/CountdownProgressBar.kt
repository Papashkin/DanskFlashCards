package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.util.toTimeFormat

@Composable
fun CountdownProgressBar(
    modifier: Modifier,
    totalTimeSec: Long,
    remainingTimeSec: Long,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(128.dp)
        ) {
            Text(
                text = remainingTimeSec.toTimeFormat(),
                modifier = Modifier.background(Color.Transparent),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineLarge
            )
            CircularProgressIndicator(
                progress = remainingTimeSec.toFloat().div(totalTimeSec),
                modifier = Modifier.size(128.dp, 128.dp),
                strokeWidth = 8.dp,
                color = MaterialTheme.colorScheme.tertiary,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CountdownProgressBarPreview() {
    CountdownProgressBar(modifier = Modifier, 120, 55)
}