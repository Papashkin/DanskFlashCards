package com.antsfamily.danskflashcards.ui.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.model.Avatar
import com.antsfamily.danskflashcards.core.model.toIconRes
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.grey_500
import com.antsfamily.danskflashcards.ui.theme.wistful_100
import com.antsfamily.danskflashcards.ui.theme.wistful_400
import com.antsfamily.danskflashcards.ui.theme.wistful_900

@Composable
fun AvatarChangeDialog(
    currentAvatar: Avatar,
    onDismiss: () -> Unit,
    onConfirmClick: (Avatar) -> Unit,
) {
    val (selectedItem, setSelectedItem) = rememberSaveable { mutableStateOf(currentAvatar) }

    Dialog(onDismissRequest = {}) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Padding.medium),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
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
                        imageVector = ImageVector.vectorResource(R.drawable.ic_gallery),
                        contentDescription = null,
                        tint = grey_500,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Pick up one of following avatars:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(Padding.medium),
                )

                LazyVerticalGrid(
                    modifier = Modifier.padding(start = 12.dp, end = 2.dp, top = 8.dp, bottom = 12.dp),
                    columns = GridCells.Adaptive(minSize = 90.dp)
                ) {
                    items(Avatar.entries) { avatar ->
                        SelectableImage(
                            modifier = Modifier.size(90.dp).padding(Padding.small),
                            imageVector = ImageVector.vectorResource(avatar.toIconRes()),
                            isSelected = avatar == selectedItem
                        ) {
                            setSelectedItem(avatar)
                        }
                    }
                }


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
                        onClick = { onConfirmClick(selectedItem) },
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
private fun AvatarChangeDialogPreview() {
    AvatarChangeDialog(
        currentAvatar = Avatar.DOLLY,
        {},
        {},
    )
}