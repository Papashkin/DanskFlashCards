package com.antsfamily.danskflashcards.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.danskflashcards.data.UserData
import com.antsfamily.danskflashcards.ui.game.view.FullScreenLoading
import com.antsfamily.danskflashcards.ui.home.model.PersonalBest
import com.antsfamily.danskflashcards.ui.home.view.PersonalBestCard
import com.antsfamily.danskflashcards.ui.theme.Padding
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun HomeScreen(
    user: UserData,
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel, HomeViewModel.Factory> {
        it.create(user = user)
    },
    navigateBack: () -> Unit,
    navigateToGame: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    when (val stateValue = state.value) {
        is HomeUiState.Loading -> FullScreenLoading()
        is HomeUiState.Content -> HomeScreenContent(
            content = stateValue,
            onStartClick = viewModel::onStartClick,
            onBackButtonClick = viewModel::onBackButtonClick
        )

        is HomeUiState.Error -> {
            //TODO add error handler
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationToGameFlow.collect {
            navigateToGame()
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
) {
    Column(
        modifier = Modifier
            .padding(Padding.medium)
            .fillMaxSize(),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null
                )
            }
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.height(Padding.large))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Welcome, ${content.userName}!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
        PersonalBestCard(data = content.personalBest) {
            onStartClick()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenContentPreview() {
    val percent = BigDecimal(32.333434).setScale(1, RoundingMode.HALF_DOWN)
    HomeScreenContent(
        HomeUiState.Content(
            userName = "Pavel Antoshkin",
            PersonalBest(43, percent, "yyyy/MM/dd HH:mm:ss")
        ), {}
    ) {}
}