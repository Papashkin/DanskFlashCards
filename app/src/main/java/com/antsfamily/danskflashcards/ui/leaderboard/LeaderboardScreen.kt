package com.antsfamily.danskflashcards.ui.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.model.Avatar
import com.antsfamily.danskflashcards.core.presentation.ErrorViewWithRetry
import com.antsfamily.danskflashcards.core.presentation.FullScreenLoading
import com.antsfamily.danskflashcards.core.presentation.TopBar
import com.antsfamily.danskflashcards.core.model.UserItem
import com.antsfamily.danskflashcards.ui.home.view.LeaderboardCard
import com.antsfamily.danskflashcards.ui.leaderboard.view.Podium
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.SetSystemBarColors
import com.antsfamily.danskflashcards.ui.theme.wistful_100

@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel = hiltViewModel<LeaderboardViewModel>(),
    navigateBack: () -> Unit,
) {
    val state = viewModel.state.collectAsState()
    SetSystemBarColors(MaterialTheme.colorScheme.background, false)

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        when (val stateValue = state.value) {
            is LeaderboardUiState.Loading -> FullScreenLoading()
            is LeaderboardUiState.Content -> LeaderboardContent(
                first = stateValue.first,
                second = stateValue.second,
                third = stateValue.third,
                others = stateValue.others
            ) {
                navigateBack()
            }

            is LeaderboardUiState.Error -> {
                ErrorViewWithRetry(errorType = stateValue.type) {
                    viewModel.onRetryClick()
                }
            }
        }
    }
}

@Composable
fun LeaderboardContent(
    first: UserItem,
    second: UserItem,
    third: UserItem,
    others: List<UserItem>,
    navigateBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        TopBar(
            onNavigationBack = { navigateBack() },
            title = stringResource(R.string.leaderboard_title),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Podium(
            modifier = Modifier.padding(horizontal = Padding.medium),
            first = first,
            second = second,
            third = third,
        )
        Spacer(modifier = Modifier.height(36.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(wistful_100, RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
                .navigationBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                items(others) { item ->
                    LeaderboardCard(item = item)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LeaderboardContentPreview() {
    LeaderboardContent(
        first = UserItem(
            id = "MockId1", username = "John", score = 44, index = 0, isCurrentUser = false, avatar = Avatar.CACTUS
        ),
        second = UserItem(
            id = "MockId2", username = "Michael", score = 32, index = 1, isCurrentUser = false, avatar = Avatar.BEAR
        ),
        third = UserItem(
            id = "MockId3", username = "Andrea", score = 29, index = 2, isCurrentUser = false, avatar = Avatar.SLOTH
        ),
        others = listOf(
            UserItem(
                id = "MockId1", username = "Sophia", score = 32, index = 3, isCurrentUser = false, avatar = Avatar.DEFAULT
            ),
            UserItem(
                id = "MockId1", username = "Noah", score = 29, index = 4, isCurrentUser = false, avatar = Avatar.DEFAULT
            ),
            UserItem(
                id = "MockId1", username = "James", score = 26, index = 5, isCurrentUser = false, avatar = Avatar.DEFAULT
            ),
            UserItem(
                id = "MockId1", username = "Sidney", score = 23, index = 6, isCurrentUser = true, avatar = Avatar.DEFAULT
            ),
            UserItem(
                id = "MockId1", username = "Anna", score = 19, index = 7, isCurrentUser = false, avatar = Avatar.DEFAULT
            ),
            UserItem(
                id = "MockId1", username = "Nicolay", score = 15, index = 8, isCurrentUser = false, avatar = Avatar.DEFAULT
            ),
            UserItem(
                id = "MockId1", username = "Alex", score = 13, index = 9, isCurrentUser = false, avatar = Avatar.DEFAULT
            )
        ), {}
    )
}