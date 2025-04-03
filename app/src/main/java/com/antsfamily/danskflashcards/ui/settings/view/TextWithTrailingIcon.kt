package com.antsfamily.danskflashcards.ui.settings.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.ui.theme.grey_500

@Composable
fun TextWithTrailingIcon(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onIconClick: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 2,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(horizontal = 10.dp),
        )
        IconButton(
            onClick = { onIconClick() },
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = grey_500,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
