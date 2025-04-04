package com.antsfamily.danskflashcards.core.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.core.model.Avatar
import com.antsfamily.danskflashcards.core.model.toIconRes
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.wistful_100
import com.antsfamily.danskflashcards.ui.theme.wistful_700

@Composable
fun AvatarIcon(
    modifier: Modifier = Modifier,
    avatar: Avatar = Avatar.DEFAULT
) {
    if (avatar.isDefault()) {
        Icon(
            imageVector = ImageVector.vectorResource(avatar.toIconRes()),
            contentDescription = null,
            tint = wistful_700,
            modifier = modifier
                .size(80.dp)
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
            modifier = modifier.size(80.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
private fun AvatarIcon1Preview() {
    AvatarIcon()
}

@Preview
@Composable
private fun AvatarIcon2Preview() {
    AvatarIcon(avatar = Avatar.CACTUS)
}