package com.antsfamily.danskflashcards.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.presentation.TopBar
import com.antsfamily.danskflashcards.core.presentation.FullScreenLoading
import com.antsfamily.danskflashcards.ui.home.model.LeaderItem
import com.antsfamily.danskflashcards.ui.home.model.LeaderboardItem
import com.antsfamily.danskflashcards.ui.home.model.UserItem
import com.antsfamily.danskflashcards.core.presentation.ErrorViewWithRetry
import com.antsfamily.danskflashcards.ui.home.view.HomeTitle
import com.antsfamily.danskflashcards.ui.home.view.LeaderboardView
import com.antsfamily.danskflashcards.ui.home.view.PersonalBestCard
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun HomeScreen(
    user: CurrentUserItem,
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel, HomeViewModel.Factory> {
        it.create(user = user)
    },
    navigateBack: () -> Unit,
    navigateToGame: (Int) -> Unit,
    navigateToAbout: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    when (val stateValue = state.value) {
        is HomeUiState.Loading -> FullScreenLoading()
        is HomeUiState.Content -> HomeScreenContent(
            content = stateValue,
            onStartClick = viewModel::onStartClick,
            onBackButtonClick = viewModel::onBackButtonClick,
            onInfoButtonClick = {
                navigateToAbout()
            }
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

    LaunchedEffect(Unit) {
        viewModel.navigationBackFlow.collect {
            navigateBack()
        }
    }
}

@Composable
fun HomeScreenContent(
    content: HomeUiState.Content,
    onStartClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    onInfoButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = Padding.medium, vertical = Padding.small)
            .fillMaxSize(),
    ) {
        //TODO implement onSettingsClick later
        TopBar {
            IconButton(
                onClick = { onInfoButtonClick() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null
                )
            }
            IconButton(
                onClick = { onBackButtonClick() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                    contentDescription = null
                )
            }
        }
        HomeTitle(
            username = content.user.name,
            isFirstTime = content.user.isFirstTime()
        )
        PersonalBestCard(
            modifier = Modifier.padding(top = Padding.large),
            score = content.user.score,
            cardsSize = content.cardsSize,
            date = content.user.date
        ) {
            onStartClick()
        }
        if (content.leaderboard != null) {
            Spacer(modifier = Modifier.height(40.dp))
            LeaderboardView(model = content.leaderboard)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenContentPreview1() {
    HomeScreenContent(
        HomeUiState.Content(
            user = UserItem(
                id = "johndoe123",
                name = "Johnny",
                surname = "Doesome",
                isCurrentUser = true,
                score = 43,
                date = "10.02.2024 11:22:33"
            ),
            leaderboard = LeaderboardItem(
                leaders = emptyList(),
                user = LeaderItem(index = 1, name = "Johnny", surname = "Doesome", score = 43)
            ),
            cardsSize = 879,
        ), {}, {}
    ) {}
}

@Preview(showBackground = true)
@Composable
fun HomeScreenContentPreview2() {
    HomeScreenContent(
        HomeUiState.Content(
            user = UserItem(
                id = "johndoe123",
                name = "Johnny",
                surname = "Doesome",
                isCurrentUser = true,
                score = 0,
                date = null
            ),
            leaderboard = LeaderboardItem(
                leaders = listOf(
                    LeaderItem(name = "John", surname = "Doe", score = 44, index = 0),
                    LeaderItem(name = "Paolo", surname = "Scoba", score = 32, index = 1),
                    LeaderItem(name = "Andrea", surname = "Corti", score = 29, index = 2),
                ),
                user = LeaderItem(name = "Pablo", surname = "Escobar", score = 12, index = 10),
            ),
            cardsSize = 879,
        ), {}, {}
    ) {}
}