package com.antsfamily.danskflashcards.ui.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.grey_500
import com.antsfamily.danskflashcards.ui.theme.wistful_100
import com.antsfamily.danskflashcards.ui.theme.wistful_500
import com.antsfamily.danskflashcards.ui.theme.wistful_700
import com.antsfamily.danskflashcards.ui.theme.wistful_800

@Composable
fun LogOutDialog(
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Padding.medium),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = wistful_100,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_sad_face),
                        contentDescription = null,
                        tint = grey_500,
                        modifier = Modifier.size(60.dp)
                    )
                }
                Text(
                    text = stringResource(R.string.logout_dialog_title),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(Padding.small),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.padding(Padding.small),
                    ) {
                        Text(
                            text = stringResource(R.string.logout_dialog_cancel),
                            style = MaterialTheme.typography.bodyLarge,
                            color = wistful_500
                        )
                    }
                    TextButton(
                        onClick = { onConfirmClick() },
                        modifier = Modifier.padding(Padding.small),
                    ) {
                        Text(
                            text = stringResource(R.string.logout_dialog_confirm),
                            style = MaterialTheme.typography.bodyLarge,
                            color = wistful_800
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LogOutDialogPreview() {
    LogOutDialog({}, {})
}