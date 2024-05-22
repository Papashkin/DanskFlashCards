package com.antsfamily.danskflashcards.ui.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.navigation.popUpToTop
import com.antsfamily.danskflashcards.ui.auth.view.ButtonWithLeadingIcon
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            viewModel.handleSignInResult(it.resultCode, it.data)
        })

    LaunchedEffect(Unit) {
        viewModel.signInFlow.collect {
            launcher.launch(IntentSenderRequest.Builder(it).build())
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationFlow.collect {
            navController.navigate(it) { popUpToTop(navController) }
        }
    }

    AuthScreenContent(state.value) {
        viewModel.onGoogleClick()
    }
}

@Composable
fun AuthScreenContent(state: AuthUiState, onGoogleClick: () -> Unit) {
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
        Image(painterResource(R.drawable.ic_home_image_bg), "null")
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        endY = 120f,
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
                        text = "Welcome!",
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "Please login to access to the cards",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = Padding.xSmall)
                    )
                }
                Column(
                    modifier = Modifier.padding(Padding.xLarge),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    ButtonWithLeadingIcon(
                        iconId = R.drawable.ic_google,
                        stringId = R.string.sign_up_google,
                        isLoading = state is AuthUiState.Loading,
                        errorText = (state as? AuthUiState.Error)?.errorMessage
                    ) {
                        onGoogleClick()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthScreenDefaultStatePreview() {
    AuthScreenContent(AuthUiState.Default) {}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthScreenLoadingStatePreview() {
    AuthScreenContent(AuthUiState.Loading) {}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthScreenErrorStatePreview() {
    AuthScreenContent(AuthUiState.Error("Test test test")) {}
}
