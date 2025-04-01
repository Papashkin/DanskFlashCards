package com.antsfamily.danskflashcards.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.presentation.ErrorViewWithRetry
import com.antsfamily.danskflashcards.core.presentation.FullScreenLoading
import com.antsfamily.danskflashcards.core.presentation.TopBar
import com.antsfamily.danskflashcards.core.util.toDisplayName
import com.antsfamily.danskflashcards.domain.model.LanguageType
import com.antsfamily.danskflashcards.ui.onboarding.model.LanguageItem
import com.antsfamily.danskflashcards.ui.settings.view.LanguageBottomSheet
import com.antsfamily.danskflashcards.ui.settings.view.LogOutDialog
import com.antsfamily.danskflashcards.ui.settings.view.SettingPreferenceView
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.grey_500
import com.antsfamily.danskflashcards.ui.theme.wistful_100
import com.antsfamily.danskflashcards.ui.theme.wistful_700

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>(),
    navigateBack: () -> Unit,
    onLogOut: () -> Unit,
) {
    val state = viewModel.state.collectAsState()

    var isLanguageBottomSheetVisible by remember { mutableStateOf(false) }
    val (languages, setLanguages) = remember { mutableStateOf<List<LanguageItem>>(emptyList()) }

    when (val stateValue = state.value) {
        is SettingsUiState.Content -> SettingsContent(
            state = stateValue,
            onLanguageClick = { viewModel.onLanguageClick() },
            onLogoutConfirm = { viewModel.onLogOutConfirm() },
            onNavigateBack = { navigateBack() }
        )

        is SettingsUiState.Error -> ErrorViewWithRetry(errorType = stateValue.type) {
            viewModel.onRetryClick()
        }

        is SettingsUiState.Loading -> FullScreenLoading()
    }

    LaunchedEffect(Unit) {
        viewModel.navigateToAuthFlow.collect {
            onLogOut()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.showLanguageBottomSheetFlow.collect {
            setLanguages(it)
            isLanguageBottomSheetVisible = true
        }
    }

    if (isLanguageBottomSheetVisible && languages.isNotEmpty()) {
        LanguageBottomSheet(
            languages = languages,
            onDismiss = {
                setLanguages(emptyList())
                isLanguageBottomSheetVisible = false
            },
            onLanguageSelected = {
                viewModel.onNewLanguageSelected(it)
            }
        )
    }
}

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    state: SettingsUiState.Content,
    onLanguageClick: () -> Unit,
    onLogoutConfirm: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    var isLogOutDialogVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Padding.medium)
    ) {
        Column {
            TopBar(
                onNavigationBack = { onNavigateBack() },
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = grey_500
                    ),
                    contentPadding = PaddingValues(0.dp),
                    onClick = { isLogOutDialogVisible = true }
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = Padding.small),
                        text = stringResource(R.string.button_logout),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            if (isLogOutDialogVisible) {
                LogOutDialog(
                    onDismiss = { isLogOutDialogVisible = false },
                    onConfirmClick = {
                        onLogoutConfirm()
                        isLogOutDialogVisible = false
                    }
                )
            }

            Column(
                modifier = modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = modifier
                            .size(80.dp)
                            .background(
                                color = wistful_100,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_avatar),
                            contentDescription = null,
                            tint = wistful_700,
                            modifier = modifier.size(60.dp)
                        )
                    }
                    Text(
                        text = state.username,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = modifier.padding(Padding.medium),
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = Padding.small),
                        text = stringResource(R.string.settings_preferences),
                        fontSize = FontSize.Body1,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge,
                        color = grey_500
                    )
                    SettingPreferenceView(
                        preferenceId = R.string.settings_pref_language,
                        leadIconId = R.drawable.ic_settings_language,
                        value = state.learningLanguage
                    ) {
                        onLanguageClick()
                    }
                    SettingPreferenceView(
                        preferenceId = R.string.settings_pref_theme,
                        leadIconId = R.drawable.ic_settings_theme,
                        valueId = R.string.settings_theme_light
                    ) {
                        //TODO implement theme switch
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Padding.large)
                        .weight(1f),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleMedium,
                        color = grey_500,
                    )
                    Text(
                        text = stringResource(R.string.about_development_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = grey_500,
                        modifier = Modifier.padding(top = Padding.xSmall),
                    )
                    Text(
                        text = stringResource(R.string.about_year),
                        style = MaterialTheme.typography.bodySmall,
                        color = grey_500,
                    )
                    Text(
                        text = state.appVersion,
                        style = MaterialTheme.typography.bodySmall,
                        color = grey_500,
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun ContentPreview() {
    SettingsContent(
        state = SettingsUiState.Content(
            username = "John Doe",
            learningLanguage = LanguageType.DE.toDisplayName(),
            appVersion = "1.0.0 (81)"
        ),
        onLogoutConfirm = {},
        onLanguageClick = {},
        onNavigateBack = {}
    )
}