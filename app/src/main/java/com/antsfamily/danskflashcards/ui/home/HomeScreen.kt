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
import com.antsfamily.danskflashcards.core.model.UserItem
import com.antsfamily.danskflashcards.core.presentation.AvatarIcon
import com.antsfamily.danskflashcards.core.presentation.ErrorViewWithRetry
import com.antsfamily.danskflashcards.core.presentation.FullScreenLoading
import com.antsfamily.danskflashcards.core.presentation.TopBar
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
    navigateToSettings: () -> Unit,
    navigateToLeaderboard: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    when (val stateValue = state.value) {
        is HomeUiState.Loading -> FullScreenLoading()
        is HomeUiState.Content -> HomeScreenContent(
            content = stateValue,
            onStartClick = { viewModel.onStartClick() },
            onSettingsClick = { navigateToSettings() },
            onLeaderboardShowAllClick = { navigateToLeaderboard() }
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
    onLeaderboardShowAllClick: () -> Unit,
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
                    .height(340.dp)
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
                    username = content.user.username,
                    color = wistful_0,
                    isFirstTime = content.user.isFirstTime()
                )
                AvatarIcon(
                    modifier = Modifier.padding(Padding.medium),
                    avatar = content.user.avatar,
                )
                CurrentUserCard(
                    modifier = Modifier.padding(horizontal = Padding.huge),
                    score = content.user.score,
                    place = content.user.place
                )
            }
            Spacer(modifier = Modifier.height(48.dp))
            LeaderboardView(items = content.leaderboard) {
                onLeaderboardShowAllClick()
            }
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
                username = "JohnnyDoesome",
                isCurrentUser = true,
                score = 0,
                avatar = Avatar.DEFAULT,
                index = null
            ),
            leaderboard = emptyList(),
        ), {}, {}, {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenContentPreview2() {
    HomeScreenContent(
        HomeUiState.Content(
            user = UserItem(
                id = "johndoe123",
                username = "JohnnyDoesome",
                isCurrentUser = true,
                score = 143,
                avatar = Avatar.CACTUS,
                index = 3
            ),
            leaderboard = listOf(
                UserItem(
                    id = "MockId1", username = "JohnDoe", score = 44, index = 0, isCurrentUser = false, avatar = Avatar.ARAGOG
                ),
                UserItem(
                    id = "MockId1", username = "PaoloScoba", score = 32, index = 1, isCurrentUser = false, avatar = Avatar.ARAGOG
                ),
                UserItem(
                    id = "MockId1", username = "AndreaCorti", score = 29, index = 2, isCurrentUser = false, avatar = Avatar.ARAGOG
                ),
                UserItem(
                    id = "MockId1", username = "EthanCaldwell", score = 29, index = 3, isCurrentUser = true, avatar = Avatar.ARAGOG
                ),
                UserItem(
                    id = "MockId1", username = "IsabellaVaughn", score = 29, index = 4, isCurrentUser = false, avatar = Avatar.ARAGOG
                ),
            ),
        ), {}, {}, {}
    )
}