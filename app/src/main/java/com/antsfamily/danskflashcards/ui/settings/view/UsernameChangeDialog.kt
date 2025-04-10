package com.antsfamily.danskflashcards.ui.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.alert
import com.antsfamily.danskflashcards.ui.theme.grey_500
import com.antsfamily.danskflashcards.ui.theme.wistful_0
import com.antsfamily.danskflashcards.ui.theme.wistful_100
import com.antsfamily.danskflashcards.ui.theme.wistful_400
import com.antsfamily.danskflashcards.ui.theme.wistful_900

private const val USERNAME_LENGTH_LIMIT = 15
@Composable
fun UsernameChangeDialog(
    value: String,
    onDismiss: () -> Unit,
    onConfirmClick: (String) -> Unit,
) {
    val (username, setUsername) = rememberSaveable { mutableStateOf(value) }

    Dialog(onDismissRequest = {}) {
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
                        .padding(Padding.large)
                        .background(
                            color = wistful_100,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_username_change),
                        contentDescription = null,
                        tint = grey_500,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = stringResource(R.string.username_change_title),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(Padding.small),
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = username,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    onValueChange = {
                        setUsername(it)
                    },
                    label = {
                        Text(stringResource(R.string.username_change_label))
                    },
                    isError = username.isBlank() || username.length > USERNAME_LENGTH_LIMIT,
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.clickable { setUsername("") },
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = wistful_0,
                        focusedContainerColor = wistful_0,
                        errorContainerColor = wistful_0,
                        errorTrailingIconColor = alert,
                        errorLabelColor = alert,
                        focusedTrailingIconColor = grey_500,
                        unfocusedTrailingIconColor = grey_500,
                        errorIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    supportingText = {
                        Text("${username.length}/$USERNAME_LENGTH_LIMIT")
                    }
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
                            text = stringResource(R.string.username_change_dialog_cancel),
                            style = MaterialTheme.typography.bodyLarge,
                            color = wistful_400
                        )
                    }
                    TextButton(
                        onClick = { onConfirmClick(username) },
                        enabled = username.isNotBlank() && username.length <= USERNAME_LENGTH_LIMIT,
                        modifier = Modifier.padding(Padding.small),
                    ) {
                        Text(
                            text = stringResource(R.string.username_change_dialog_confirm),
                            style = MaterialTheme.typography.bodyLarge,
                            color = wistful_900
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun UsernameChangeDialogPreview() {
    UsernameChangeDialog("John long-name Doe", {}, {})
}