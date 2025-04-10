package com.antsfamily.danskflashcards.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.model.Avatar
import com.antsfamily.danskflashcards.core.presentation.AvatarIcon
import com.antsfamily.danskflashcards.core.presentation.ErrorViewWithRetry
import com.antsfamily.danskflashcards.core.presentation.FullScreenLoading
import com.antsfamily.danskflashcards.core.presentation.TopBar
import com.antsfamily.danskflashcards.core.util.toErrorMessage
import com.antsfamily.danskflashcards.core.util.toStringRes
import com.antsfamily.danskflashcards.domain.model.LanguageType
import com.antsfamily.danskflashcards.ui.onboarding.model.LanguageItem
import com.antsfamily.danskflashcards.ui.settings.view.AvatarChangeDialog
import com.antsfamily.danskflashcards.ui.settings.view.LanguageBottomSheet
import com.antsfamily.danskflashcards.ui.settings.view.LogOutDialog
import com.antsfamily.danskflashcards.ui.settings.view.SettingPreferenceView
import com.antsfamily.danskflashcards.ui.settings.view.TextWithTrailingIcon
import com.antsfamily.danskflashcards.ui.settings.view.UsernameChangeDialog
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.SetSystemBarColors
import com.antsfamily.danskflashcards.ui.theme.grey_500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>(),
    navigateBack: () -> Unit,
    onErrorSnackbarShow: (String) -> Unit,
    onLogOut: () -> Unit,
) {
    SetSystemBarColors(MaterialTheme.colorScheme.background, false)

    var isLearningLanguageBottomSheetVisible by remember { mutableStateOf(false) }
    var isPrimaryLanguageBottomSheetVisible by remember { mutableStateOf(false) }
    val (isAvatarChangeDialogVisible, setIsAvatarChangeDialogVisible) = remember {
        mutableStateOf(false)
    }
    val (languages, setLanguages) = remember { mutableStateOf<List<LanguageItem>>(emptyList()) }

    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    when (val stateValue = state.value) {
        is SettingsUiState.Content -> SettingsContent(
            state = stateValue,
            onLearningLanguageClick = { viewModel.onLanguageClick(false) },
            onPrimaryLanguageClick = { viewModel.onLanguageClick(true) },
            onLogoutConfirm = { viewModel.onLogOutConfirm() },
            onNavigateBack = { navigateBack() },
            onUsernameChanged = { viewModel.onUsernameChanged(it) },
            onAvatarChangeClick = { viewModel.onAvatarChangeClick() }
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
        viewModel.showLearningLanguageBottomSheetFlow.collect {
            setLanguages(it)
            isLearningLanguageBottomSheetVisible = true
        }
    }
    LaunchedEffect(Unit) {
        viewModel.showPrimaryLanguageBottomSheetFlow.collect {
            setLanguages(it)
            isPrimaryLanguageBottomSheetVisible = true
        }
    }
    LaunchedEffect(Unit) {
        viewModel.showAvatarChangeDialogFlow.collect {
            setIsAvatarChangeDialogVisible(true)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.showErrorSnackbarFlow.collect {
            onErrorSnackbarShow(context.getString(it.toErrorMessage()))
        }
    }

    if (isLearningLanguageBottomSheetVisible && languages.isNotEmpty()) {
        LanguageBottomSheet(
            languages = languages,
            onDismiss = {
                setLanguages(emptyList())
                isLearningLanguageBottomSheetVisible = false
            },
            isPrimary = false,
            onLanguageSelected = {
                viewModel.onNewLanguageSelected(it, false)
            }
        )
    }
    if (isPrimaryLanguageBottomSheetVisible && languages.isNotEmpty()) {
        LanguageBottomSheet(
            languages = languages,
            onDismiss = {
                setLanguages(emptyList())
                isPrimaryLanguageBottomSheetVisible = false
            },
            isPrimary = true,
            onLanguageSelected = {
                viewModel.onNewLanguageSelected(it, true)
            }
        )
    }
    if (isAvatarChangeDialogVisible) {
        AvatarChangeDialog(
            currentAvatar = (state.value as SettingsUiState.Content).avatar,
            onDismiss = {
                setIsAvatarChangeDialogVisible(false)
            },
            onConfirmClick = {
                setIsAvatarChangeDialogVisible(false)
                viewModel.onAvatarSelected(it)
            }
        )
    }
}

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    state: SettingsUiState.Content,
    onLearningLanguageClick: () -> Unit,
    onPrimaryLanguageClick: () -> Unit,
    onLogoutConfirm: () -> Unit,
    onNavigateBack: () -> Unit,
    onUsernameChanged: (String) -> Unit,
    onAvatarChangeClick: () -> Unit
) {
    val (isLogOutDialogVisible, setIsLogOutDialogVisible) = remember { mutableStateOf(false) }
    val (isUsernameDialogVisible, setIsUsernameDialogVisible) = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column {
            TopBar(
                modifier = Modifier.padding(end = Padding.small),
                onNavigationBack = { onNavigateBack() },
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = grey_500
                    ),
                    contentPadding = PaddingValues(0.dp),
                    onClick = { setIsLogOutDialogVisible(true) }
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = Padding.small),
                        text = stringResource(R.string.settings_button_logout),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            if (isLogOutDialogVisible) {
                LogOutDialog(
                    onDismiss = { setIsLogOutDialogVisible(false) },
                    onConfirmClick = {
                        onLogoutConfirm()
                        setIsLogOutDialogVisible(false)
                    }
                )
            }

            if (isUsernameDialogVisible) {
                UsernameChangeDialog(
                    value = state.username,
                    onDismiss = { setIsUsernameDialogVisible(false) },
                    onConfirmClick = {
                        onUsernameChanged(it)
                        setIsUsernameDialogVisible(false)
                    }
                )
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = Padding.huge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Padding.medium),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AvatarIcon(
                        modifier = Modifier.padding(vertical = Padding.medium),
                        avatar = state.avatar
                    ) {
                        onAvatarChangeClick()
                    }
                    TextWithTrailingIcon(
                        modifier = modifier.padding(Padding.medium),
                        text = state.username,
                        icon = Icons.Rounded.Edit
                    ) {
                        setIsUsernameDialogVisible(true)
                    }
                }
                Spacer(Modifier.height(60.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Padding.medium),
                    verticalArrangement = Arrangement.Top,
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
                        title = stringResource(R.string.settings_pref_language),
                        subtitle = stringResource(R.string.preference_subtitle_learning),
                        leadIcon = ImageVector.vectorResource(R.drawable.ic_settings_language),
                        valueId = state.learningLanguage.toStringRes()
                    ) {
                        onLearningLanguageClick()
                    }

                    SettingPreferenceView(
                        title = stringResource(R.string.settings_pref_language),
                        subtitle = stringResource(R.string.preference_subtitle_primary),
                        leadIcon = ImageVector.vectorResource(R.drawable.ic_settings_language),
                        valueId = state.primaryLanguage.toStringRes()
                    ) {
                        onPrimaryLanguageClick()
                    }
                    //TODO implement theme switch
//                    SettingPreferenceView(
//                        title = stringResource(R.string.settings_pref_theme),
//                        leadIcon = ImageVector.vectorResource(R.drawable.ic_settings_theme),
//                        valueId = R.string.settings_theme_light
//                    ) {
//                    }
                }
                Spacer(Modifier.weight(2f))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleMedium,
                        color = grey_500,
                    )
                    Text(
                        text = stringResource(R.string.settings_development_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = grey_500,
                        modifier = Modifier.padding(top = Padding.xSmall),
                    )
                    Text(
                        text = stringResource(R.string.settings_year),
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


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ContentPreview() {
    SettingsContent(
        state = SettingsUiState.Content(
            username = "Long-lasting Partymaker Doe",
            learningLanguage = LanguageType.DE,
            primaryLanguage = LanguageType.EN,
            appVersion = "1.0.0 (81)",
            avatar = Avatar.AVOCADO
        ),
        onLogoutConfirm = {},
        onLearningLanguageClick = {},
        onPrimaryLanguageClick = {},
        onNavigateBack = {},
        onAvatarChangeClick = {},
        onUsernameChanged = {}
    )
}