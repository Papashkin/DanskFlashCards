package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.antsfamily.danskflashcards.R

@Composable
fun HomeTitle(
    modifier: Modifier = Modifier,
    color: Color,
    username: String,
    isFirstTime: Boolean
) {
    Column(
        modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = modifier.fillMaxWidth(),
            text = if (isFirstTime) {
                stringResource(R.string.home_title_welcome, username)
            } else {
                stringResource(R.string.home_title_welcome_back, username)
            },
            style = MaterialTheme.typography.headlineSmall,
            color = color,
            textAlign = TextAlign.Center
        )
    }
}