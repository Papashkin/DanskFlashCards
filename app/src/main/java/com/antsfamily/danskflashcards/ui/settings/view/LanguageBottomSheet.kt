package com.antsfamily.danskflashcards.ui.settings.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.util.toFlagIconRes
import com.antsfamily.danskflashcards.core.util.toStringRes
import com.antsfamily.danskflashcards.domain.model.LanguageType
import com.antsfamily.danskflashcards.ui.onboarding.model.LanguageItem
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.wistful_1000

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageBottomSheet(
    languages: List<LanguageItem>,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onLanguageSelected: (LanguageItem) -> Unit,
    onDismiss: () -> Unit
) {
    val (selectedItem, setSelectedItem) = remember { mutableStateOf(languages.first { it.isSelected }) }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        shape = RoundedCornerShape(Padding.large, Padding.large, 0.dp, 0.dp),
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(horizontal = Padding.medium),
                text = "Please select learning language",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            LazyColumn(
                modifier = Modifier.padding(vertical = Padding.large),
                verticalArrangement = Arrangement.spacedBy(Padding.xSmall)
            ) {
                items(languages) { item ->
                    ListItem(
                        modifier = Modifier.clickable { setSelectedItem(item) },
                        headlineContent = {
                            Text(
                                text = stringResource(item.languageType.toStringRes()),
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = Padding.small),
                                color = wistful_1000
                            )
                        },
                        leadingContent = {
                            Image(
                                imageVector = ImageVector.vectorResource(item.languageType.toFlagIconRes()),
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape),
                                contentDescription = null
                            )
                        },
                        trailingContent = {
                            RadioButton(
                                selected = item == selectedItem,
                                onClick = { setSelectedItem(item) }
                            )
                        },
                        colors = ListItemColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            headlineColor = MaterialTheme.colorScheme.primary,
                            supportingTextColor = MaterialTheme.colorScheme.secondary,
                            leadingIconColor = MaterialTheme.colorScheme.onSurface,
                            trailingIconColor = MaterialTheme.colorScheme.onSurface,
                            overlineColor = Color.Transparent,
                            disabledHeadlineColor = Color.Gray,
                            disabledLeadingIconColor = Color.Gray,
                            disabledTrailingIconColor = Color.Gray
                        ),
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .width(300.dp)
                    .padding(Padding.large),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(Padding.large),
                onClick = {
                    onLanguageSelected(selectedItem)
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(R.string.settings_language_selection_confirm),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LanguageBottomSheetPreview(modifier: Modifier = Modifier) {
    LanguageBottomSheet(
        languages = listOf(
            LanguageType.DE,
            LanguageType.DK,
            LanguageType.RU
        ).map { LanguageItem(it, false) },
        onLanguageSelected = {},
        onDismiss = {}
    )
}

