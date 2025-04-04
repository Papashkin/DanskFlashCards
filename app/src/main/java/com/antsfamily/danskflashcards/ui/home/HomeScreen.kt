package com.antsfamily.danskflashcards.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.model.Avatar
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.presentation.AvatarIcon
import com.antsfamily.danskflashcards.core.presentation.ErrorViewWithRetry
import com.antsfamily.danskflashcards.core.presentation.FullScreenLoading
import com.antsfamily.danskflashcards.core.presentation.TopBar
import com.antsfamily.danskflashcards.ui.home.model.LeaderItem
import com.antsfamily.danskflashcards.ui.home.model.UserItem
import com.antsfamily.danskflashcards.ui.home.view.CurrentUserCard
import com.antsfamily.danskflashcards.ui.home.view.HomeTitle
import com.antsfamily.danskflashcards.ui.home.view.LeaderboardView
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.SetSystemBarColors
import com.antsfamily.danskflashcards.ui.theme.wistful_0
import com.antsfamily.danskflashcards.ui.theme.wistful_500
import com.antsfamily.danskflashcards.ui.theme.wistful_600
import com.antsfamily.danskflashcards.ui.theme.wistful_700

@Composable
fun HomeScreen(
    user: CurrentUserItem,
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel, HomeViewModel.Factory> {
        it.create(user = user)
    },
    navigateToGame: (Int) -> Unit,
    navigateToSettings: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    when (val stateValue = state.value) {
        is HomeUiState.Loading -> FullScreenLoading()
        is HomeUiState.Content -> HomeScreenContent(
            content = stateValue,
            onStartClick = viewModel::onStartClick,
            onSettingsClick = { navigateToSettings() }
        )

        is HomeUiState.Error -> {
            ErrorViewWithRetry(errorType = stateValue.type) {
                viewModel.onRetryClick()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationToGameFlow.collect {
            navigateToGame(it)
        }
    }
}

@Composable
fun HomeScreenContent(
    content: HomeUiState.Content,
    onStartClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    SetSystemBarColors(wistful_700, true)

    Column(
        modifier = Modifier
            .background(wistful_700)
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(
                        color = wistful_700,
                        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopBar(modifier = Modifier.padding(end = Padding.small)) {
                    IconButton(
                        colors = IconButtonColors(
                            containerColor = wistful_600,
                            contentColor = wistful_0,
                            disabledContentColor = wistful_500,
                            disabledContainerColor = wistful_0
                        ),
                        onClick = { onSettingsClick() }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_home_settings),
                            modifier = Modifier.size(24.dp),
                            contentDescription = null
                        )
                    }
                }
                HomeTitle(
                    username = content.user.name,
                    color = wistful_0,
                    isFirstTime = content.user.isFirstTime()
                )
                AvatarIcon(
                    modifier = Modifier.padding(vertical = Padding.medium),
                    avatar = content.user.avatar
                )
                CurrentUserCard(
                    modifier = Modifier.padding(
                        horizontal = Padding.huge,
                        vertical = Padding.xSmall
                    ),
                    score = content.user.score,
                    cardsSize = content.cardsSize,
                    place = content.userPlace
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            LeaderboardView(items = content.leaderboard)
            Spacer(modifier = Modifier
                .height(24.dp)
                .weight(1f))
            Button(
                onClick = { onStartClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = Padding.huge)
            ) {
                Text(
                    text = stringResource(R.string.home_start),
                    fontSize = FontSize.H6,
                )
            }
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenContentPreview1() {
    HomeScreenContent(
        HomeUiState.Content(
            user = UserItem(
                id = "johndoe123",
                name = "Johnny",
                surname = "Doesome",
                isCurrentUser = true,
                score = 0,
                date = null,
                avatar = null
            ),
            leaderboard = emptyList(),
            cardsSize = 1000,
            userPlace = null
        ), {}, {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenContentPreview2() {
    HomeScreenContent(
        HomeUiState.Content(
            user = UserItem(
                id = "johndoe123",
                name = "Johnny",
                surname = "Doesome",
                isCurrentUser = true,
                score = 143,
                date = "10.02.2024 11:22:33",
                avatar = Avatar.CACTUS
            ),
            leaderboard = listOf(
                LeaderItem(
                    name = "John", surname = "Doe", score = 44, index = 0, isUser = false
                ),
                LeaderItem(
                    name = "Paolo", surname = "Scoba", score = 32, index = 1, isUser = false
                ),
                LeaderItem(
                    name = "Andrea", surname = "Corti", score = 29, index = 2, isUser = false
                ),
                LeaderItem(
                    name = "Ethan", surname = "Caldwell", score = 29, index = 3, isUser = true
                ),
                LeaderItem(
                    name = "Isabella", surname = "Vaughn", score = 29, index = 4, isUser = false
                ),
            ),
            cardsSize = 1000,
            userPlace = 3
        ), {}, {}
    )
}