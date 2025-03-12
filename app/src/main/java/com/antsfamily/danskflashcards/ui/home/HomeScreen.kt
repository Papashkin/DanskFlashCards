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
import com.antsfamily.danskflashcards.core.presentation.TopBar
import com.antsfamily.danskflashcards.ui.auth.CurrentUserModel
import com.antsfamily.danskflashcards.ui.game.view.FullScreenLoading
import com.antsfamily.danskflashcards.ui.home.model.LeaderboardItem
import com.antsfamily.danskflashcards.ui.home.model.LeaderboardModel
import com.antsfamily.danskflashcards.ui.home.model.UserModel
import com.antsfamily.danskflashcards.ui.home.view.HomeTitle
import com.antsfamily.danskflashcards.ui.home.view.LeaderboardView
import com.antsfamily.danskflashcards.ui.home.view.PersonalBestCard
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun HomeScreen(
    user: CurrentUserModel,
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
            //TODO add error handler
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
            user = UserModel(
                id = "johndoe123",
                name = "Johnny",
                surname = "Doesome",
                isCurrentUser = true,
                score = 43,
                date = "10.02.2024 11:22:33"
            ),
            leaderboard = LeaderboardModel(
                leaders = listOf(
                    LeaderboardItem(name = "Pablo", surname = "Escobar", score = 44, index = 0),
                    LeaderboardItem(name = "Carlos", surname = "Esteves", score = 32, index = 1),
                    LeaderboardItem(name = "Andrea", surname = "Corti", score = 29, index = 2),
                ),
                user = LeaderboardItem(
                    name = "Johnny",
                    surname = "Doesome",
                    score = 12,
                    index = 10
                ),
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
            user = UserModel(
                id = "johndoe123",
                name = "Johnny",
                surname = "Doesome",
                isCurrentUser = true,
                score = 0,
                date = null
            ),
            leaderboard = LeaderboardModel(
                leaders = listOf(
                    LeaderboardItem(name = "John", surname = "Doe", score = 44, index = 0),
                    LeaderboardItem(name = "Pablo", surname = "Escobar", score = 32, index = 1),
                    LeaderboardItem(name = "Andrea", surname = "Corti", score = 29, index = 2),
                ),
                user = LeaderboardItem(name = "Pablo", surname = "Escobar", score = 12, index = 10),
            ),
            cardsSize = 879,
        ), {}, {}
    ) {}
}