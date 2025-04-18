package com.antsfamily.danskflashcards.ui.onboarding2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.presentation.ButtonWithLoading
import com.antsfamily.danskflashcards.core.presentation.FullScreenLoading
import com.antsfamily.danskflashcards.core.util.toFlagIconRes
import com.antsfamily.danskflashcards.core.util.toStringRes
import com.antsfamily.danskflashcards.domain.model.LanguageType
import com.antsfamily.danskflashcards.ui.onboarding.model.LanguageItem
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.wistful_100
import com.antsfamily.danskflashcards.ui.theme.wistful_1000
import com.antsfamily.danskflashcards.ui.theme.wistful_200
import com.antsfamily.danskflashcards.ui.theme.wistful_400

@Composable
fun Onboarding2Screen(
    user: CurrentUserItem,
    viewModel: Onboarding2ViewModel = hiltViewModel<Onboarding2ViewModel>(),
    onNavigateToHome: (CurrentUserItem) -> Unit
) {

    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationToHomeFlow.collect {
            onNavigateToHome(user)
        }
    }

    when (val stateValue = state.value) {
        is Onboarding2UiState.Content -> Onboarding2Content(
            state = stateValue,
            onLanguageSelected = { viewModel.onLanguageSelected(it) },
            onContinueClick = { viewModel.onContinueClick() }
        )

        is Onboarding2UiState.Loading -> FullScreenLoading()
    }
}

@Composable
fun Onboarding2Content(
    state: Onboarding2UiState.Content,
    onLanguageSelected: (LanguageItem) -> Unit,
    onContinueClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f),
            imageVector = ImageVector.vectorResource(R.drawable.ic_onboarding_2),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .weight(1.0f)
                .background(
                    brush = Brush.verticalGradient(
                        endY = 120f,
                        colors = listOf(
                            wistful_400,
                            wistful_200,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = Padding.medium, end = Padding.medium, top = Padding.small),
                verticalArrangement = Arrangement.Bottom
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Padding.small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = stringResource(R.string.onboarding2_title),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = stringResource(R.string.onboarding2_subtitle),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = Padding.medium)
                    )
                }
                LazyColumn(
                    modifier = Modifier.weight(1f).padding(vertical = Padding.medium),
                    verticalArrangement = Arrangement.spacedBy(Padding.small)
                ) {
                    items(state.languages) { item ->
                        ListItem(
                            modifier = Modifier
                                .height(48.dp)
                                .clickable { onLanguageSelected(item) },
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
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .border(0.5.dp, color = wistful_100, shape = CircleShape)
                                    ,
                                    contentDescription = null
                                )
                            },
                            trailingContent = {
                                RadioButton(
                                    selected = item.isSelected,
                                    onClick = { onLanguageSelected(item) }
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
                ButtonWithLoading(
                    modifier = Modifier.padding(Padding.huge),
                    isEnabled = state.isButtonAvailable,
                    stringId = R.string.onboarding_button_continue,
                    isLoading = state.isButtonLoadingVisible
                ) {
                    onContinueClick()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingPreview() {
    Onboarding2Content(
        state = Onboarding2UiState.Content(
            languages = listOf(
                LanguageType.DE,
                LanguageType.DK,
                LanguageType.RU
            ).map { LanguageItem(it, false) },
            true,
            false,
        ), {}, {}
    )
}