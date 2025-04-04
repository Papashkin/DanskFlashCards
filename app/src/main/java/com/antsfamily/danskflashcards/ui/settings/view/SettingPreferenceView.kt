package com.antsfamily.danskflashcards.ui.settings.view

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.grey_200
import com.antsfamily.danskflashcards.ui.theme.grey_500

@Composable
fun SettingPreferenceView(
    modifier: Modifier = Modifier,
    leadIcon: ImageVector,
    title: String,
    subtitle: String? = null,
    @StringRes valueId: Int? = null,
    value: String? = null,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
            headlineColor = MaterialTheme.colorScheme.onBackground,
            trailingIconColor = grey_500
        ),
        headlineContent = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = Padding.small),
            )
        },
        supportingContent = {
            subtitle?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(start = Padding.small),
                )
            }
        },
        leadingContent = {
            Box(
                modifier = modifier
                    .size(48.dp)
                    .background(
                        color = grey_200,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = leadIcon,
                    tint = grey_500,
                    contentDescription = null,
                    modifier = modifier.size(32.dp)
                )
            }
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = valueId?.let { stringResource(it) } ?: value.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
                Icon(
                    Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = modifier.size(24.dp)
                )
            }
        },
    )
}

@Preview
@Composable
private fun SettingPreferenceViewPreview() {
    SettingPreferenceView(
        title = stringResource(R.string.settings_pref_language),
        leadIcon = ImageVector.vectorResource(R.drawable.ic_settings_language,),
        valueId = R.string.language_dk,
    ) {}
}