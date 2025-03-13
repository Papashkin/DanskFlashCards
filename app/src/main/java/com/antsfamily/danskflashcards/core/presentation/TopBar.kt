package com.antsfamily.danskflashcards.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onNavigationBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier.padding(end = Padding.small),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { onNavigationBack?.invoke() }) {
                if (onNavigationBack != null) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }
            }
            if (title != null) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        title,
                        maxLines = 1,
                        fontSize = FontSize.H6,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
        Row {
            actions()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview1() {
    TopBar(
        title = "Some very long long title",
        onNavigationBack = {},
    ) {
        IconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null
            )
        }
        IconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun TopBarPreview2() {
    TopBar(
        onNavigationBack = {},
    ) {
        IconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null
            )
        }
        IconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun TopBarPreview3() {
    TopBar(
        title = "Something else",
    ) {
        IconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview4() {
    TopBar {
        IconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null
            )
        }
        IconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                contentDescription = null
            )
        }
    }
}