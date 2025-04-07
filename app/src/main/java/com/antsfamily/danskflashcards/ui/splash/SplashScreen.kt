package com.antsfamily.danskflashcards.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.antsfamily.danskflashcards.MainActivity
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.model.CurrentUserItem

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    navigateToAuth: () -> Unit,
    navigateToHome: (CurrentUserItem) -> Unit,
    navigateToOnboarding: (CurrentUserItem) -> Unit,
) {
    val activity = LocalContext.current as? MainActivity

    LaunchedEffect(Unit) {
        viewModel.navigationToAuthFlow.collect {
            navigateToAuth()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.navigationToHomeFlow.collect {
            navigateToHome(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.navigationToOnboardingFlow.collect {
            navigateToOnboarding(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.startAppUpdateFlow.collect {
            activity?.startAppUpdate(it)
        }
    }

    SplashViewWithIcon()
}

@Composable
fun SplashViewWithIcon() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = R.drawable.ic_launcher,
                contentDescription = null,
                modifier = Modifier.size(180.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashViewWithIconPreview() {
    SplashViewWithIcon()
}
