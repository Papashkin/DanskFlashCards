package com.antsfamily.danskflashcards.ui.about

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.presentation.TopBar
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun AboutScreen(
    viewModel: AboutViewModel = hiltViewModel<AboutViewModel>(),
    navigateBack: () -> Unit,
) {
    val state = viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Padding.medium, vertical = Padding.small)
    ) {
        TopBar(onNavigationBack = navigateBack)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Card(
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                AsyncImage(
                    model = R.drawable.ic_about_icon,
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = Padding.medium),
            )

            Text(
                text = stringResource(R.string.about_development_description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = Padding.large),
            )
            Text(
                text = stringResource(R.string.about_year),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = Padding.xSmall),
            )

            Spacer(modifier = Modifier.weight(1f))

            (state.value as? AboutUiState.Content)?.version?.let {
                Text(
                    text = stringResource(R.string.about_app_version, it),
                    fontSize = FontSize.Caption,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = Padding.large)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    AboutScreen {}
}
