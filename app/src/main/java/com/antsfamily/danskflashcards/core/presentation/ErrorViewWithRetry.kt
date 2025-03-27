package com.antsfamily.danskflashcards.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.core.model.toErrorMessage
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.light_accent

@Composable
fun ErrorViewWithRetry(
    modifier: Modifier = Modifier,
    errorType: ErrorType,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Rounded.Warning,
            contentDescription = null,
            tint = light_accent,
            modifier = modifier.size(60.dp)
        )
        Text(
            text = stringResource(errorType.toErrorMessage()),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(Padding.large),
        )
        Box(
            modifier = modifier.padding(Padding.large)
        ) {
            Button(
                modifier = Modifier
                    .width(200.dp)
                    .padding(Padding.small),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(Padding.large),
                onClick = { onRetry() }
            ) {
                Text(
                    text = stringResource(R.string.button_retry),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorViewWithRetryPreview() {
    ErrorViewWithRetry(errorType = ErrorType.NetworkConnection) {}
}