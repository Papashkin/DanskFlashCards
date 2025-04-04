package com.antsfamily.danskflashcards.core.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.core.model.Avatar
import com.antsfamily.danskflashcards.core.model.toIconRes
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.grey_200
import com.antsfamily.danskflashcards.ui.theme.grey_500
import com.antsfamily.danskflashcards.ui.theme.wistful_100
import com.antsfamily.danskflashcards.ui.theme.wistful_700

@Composable
fun AvatarIcon(
    modifier: Modifier = Modifier,
    avatar: Avatar = Avatar.DEFAULT,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .clickable(
                interactionSource = null,
                indication = null,
            ) { onClick?.invoke() }

    ) {
        if (avatar.isDefault()) {
            Icon(
                imageVector = ImageVector.vectorResource(avatar.toIconRes()),
                contentDescription = null,
                tint = wistful_700,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Padding.tiny)
                    .background(
                        color = wistful_100,
                        shape = CircleShape
                    ),
            )
        } else {
            Image(
                ImageVector.vectorResource(avatar.toIconRes()),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        if (onClick != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(24.dp)
                    .border(1.dp, grey_200, CircleShape)
                    .background(MaterialTheme.colorScheme.background, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = null,
                    tint = grey_500,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(Padding.tiny)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AvatarIcon1Preview() {
    AvatarIcon {}
}

@Preview
@Composable
private fun AvatarIcon2Preview() {
    AvatarIcon(avatar = Avatar.CACTUS) {}
}