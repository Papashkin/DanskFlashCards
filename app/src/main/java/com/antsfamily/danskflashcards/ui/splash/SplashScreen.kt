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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.antsfamily.danskflashcards.R

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    navigateToAuth: () -> Unit,
) {
    val isUpdateAvailable: Boolean by viewModel.updateAvailabilityFlow.collectAsState(false)

    LaunchedEffect(Unit) {
        viewModel.navigationToAuthFlow.collect {
            navigateToAuth()
        }
    }

    if (isUpdateAvailable) {
        UpdateDialog(
            onDismiss = { viewModel.onDismissClick() },
            onUpdateClick = { viewModel.onUpdateClick() }
        )
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

@Composable
fun UpdateDialog(
    onUpdateClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(stringResource(R.string.dialog_update_title)) },
            text = { Text(stringResource(R.string.dialog_update_subtitle)) },
            confirmButton = {
                Button(onClick = { onUpdateClick() }) {
                    Text(stringResource(R.string.dialog_update_button_update))
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text(stringResource(R.string.dialog_update_button_cancel))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashViewWithIconPreview() {
    SplashViewWithIcon()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun UpdateDialogPreview() {
    UpdateDialog(
        onDismiss = {},
        onUpdateClick = {}
    )
}