package com.antsfamily.danskflashcards.ui.game.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.grey_500
import com.antsfamily.danskflashcards.ui.theme.wistful_100

@Composable
fun QuitGameDialog(
    onExitConfirm: () -> Unit,
    onStayClick: () -> Unit,
) {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(Padding.large)
                        .background(
                            color = wistful_100,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_pockerface),
                        contentDescription = null,
                        tint = grey_500,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    modifier = Modifier.padding(Padding.medium),
                    text = stringResource(R.string.quit_game_dialog_title),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Padding.medium),
                    text = stringResource(R.string.quit_game_dialog_subtitle),
                    textAlign = TextAlign.Center,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Padding.huge),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Button(
                        onClick = { onExitConfirm() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(stringResource(R.string.quit_game_dialog_button_exit))
                    }
                    Button(
                        onClick = { onStayClick() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(stringResource(R.string.quit_game_dialog_button_stay))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun QuitGameDialogPreview() {
    QuitGameDialog({}, {})
}