package com.antsfamily.danskflashcards.core.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.wistful_100

@Composable
fun ButtonWithLoading(
    modifier: Modifier = Modifier,
    stringId: Int,
    isLoading: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = isEnabled,
        contentPadding = PaddingValues(Padding.none),
        onClick = { if (!isLoading) onClick() },
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                Modifier.size(24.dp),
                strokeWidth = 2.dp,
                color = wistful_100,
            )
        } else {
            Text(
                text = stringResource(id = stringId),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonWithLoadingPreview() {
    Column(Modifier.fillMaxSize()) {
        ButtonWithLoading(
            Modifier,
            R.string.onboarding_button_continue,
            false,
            true,
        ) {}
        Spacer(modifier = Modifier.height(20.dp))
        ButtonWithLoading(
            Modifier,
            R.string.onboarding_button_continue,
            false,
            false,
        ) {}
        Spacer(modifier = Modifier.height(20.dp))
        ButtonWithLoading(
            Modifier,
            R.string.onboarding_button_continue,
            true,
            true,
        ) {}
        Spacer(modifier = Modifier.height(20.dp))
    }
}