package com.antsfamily.danskflashcards.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.model.toErrorMessage
import com.antsfamily.danskflashcards.ui.auth.AuthUiState
import com.antsfamily.danskflashcards.ui.auth.view.ButtonWithLeadingIcon
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun OnboardingScreen(
    user: CurrentUserItem,
    viewModel: OnboardingViewModel = hiltViewModel<OnboardingViewModel, OnboardingViewModel.Factory> {
        it.create(user = user)
    },
    onNavigateToHome: (CurrentUserItem) -> Unit
) {

    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationToHomeFlow.collect { user ->
            onNavigateToHome(user)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    endY = 20f,
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        Color(0xffa0cfff),
                    )
                )
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(painterResource(R.drawable.ic_onboarding), null)
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        endY = 200f,
                        colors = listOf(
                            Color(0xff7CBBff),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.auth_title),
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = stringResource(R.string.auth_subtitle),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = Padding.xSmall)
                    )
                }
                Column(
                    modifier = Modifier.padding(Padding.xLarge),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        contentPadding = PaddingValues(0.dp),
                        onClick = { onNavigateToHome(user) },
                    ) {
                        Text("Continue")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingPreview() {
    OnboardingScreen(
        CurrentUserItem(
            userId = "asldkhajsflkadjfla",
            username = "Johnny Doesome",
        ),
    ) {}
}