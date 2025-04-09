package com.antsfamily.danskflashcards.ui.leaderboard.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.core.model.Avatar
import com.antsfamily.danskflashcards.core.model.toIconRes
import com.antsfamily.danskflashcards.core.util.mapToColor
import com.antsfamily.danskflashcards.core.model.UserItem
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.wistful_100
import com.antsfamily.danskflashcards.ui.theme.wistful_700

@Composable
fun PodiumAvatarIcon(
    modifier: Modifier = Modifier,
    item: UserItem
) {
    Box(
        modifier = modifier.size(100.dp)
    ) {
        if (item.avatar.isDefault()) {
            Icon(
                imageVector = ImageVector.vectorResource(item.avatar.toIconRes()),
                contentDescription = null,
                tint = wistful_700,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Padding.tiny)
                    .border(4.dp, item.mapToColor(), CircleShape)
                    .background(
                        color = wistful_100,
                        shape = CircleShape
                    ),
            )
        } else {
            Image(
                ImageVector.vectorResource(item.avatar.toIconRes()),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .border(4.dp, item.mapToColor(), CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(32.dp)
                .offset(y = (10).dp)
                .border(4.dp, item.mapToColor(), CircleShape)
                .background(MaterialTheme.colorScheme.background, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.place.toString(),
                color = item.mapToColor(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium

            )
        }
    }
}

@Preview
@Composable
private fun PodiumAvatarIcon1Preview() {
    PodiumAvatarIcon(
        item = UserItem(
            id = "MockId1",
            username = "John",
            score = 44,
            index = 0,
            isCurrentUser = false,
            avatar = Avatar.CACTUS
        )
    )
}

@Preview
@Composable
private fun PodiumAvatarIcon2Preview() {
    PodiumAvatarIcon(
        item = UserItem(
            id = "MockId2",
            username = "John",
            score = 44,
            index = 1,
            isCurrentUser = false,
            avatar = Avatar.DEFAULT
        )
    )
}

@Preview
@Composable
private fun PodiumAvatarIcon3Preview() {
    PodiumAvatarIcon(
        item = UserItem(
            id = "MockId3",
            username = "John",
            score = 44,
            index = 2,
            isCurrentUser = false,
            avatar = Avatar.SLOTH
        )
    )
}