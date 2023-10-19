package com.antsfamily.danskflashcards.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.data.WordModel
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    when (val stateValue = state.value) {
        HomeUiState.Loading -> FullScreenLoading()
        is HomeUiState.Content -> HomeScreenPostsContent(stateValue.danish, stateValue.english)
        is HomeUiState.Error -> {
            //TODO add error handler
        }
    }
}

@Composable
fun HomeScreenPostsContent(danish: List<WordModel>, english: List<WordModel>) {
    Column(modifier = Modifier.padding(horizontal = Padding.xLarge, vertical = Padding.medium)) {
        Text(
            text = stringResource(id = R.string.home_title),
            fontSize = FontSize.H3,
            modifier = Modifier
                .padding(top = Padding.medium)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(id = R.string.home_description),
            fontSize = FontSize.Body1,
            modifier = Modifier
                .padding(top = Padding.medium)
                .align(Alignment.CenterHorizontally)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Padding.medium)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = Padding.small)
                ) {
                    GridWithCards(danish)
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = Padding.small)
                ) {
                    GridWithCards(english)
                }
            }
        }
    }
}

@Composable
fun GridWithCards(words: List<WordModel>) {
    LazyColumn {
        items(words.size) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.LightGray, RoundedCornerShape(Padding.medium))
            ) {
                Box(
                    modifier = Modifier.padding(vertical = Padding.medium)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = Padding.xLarge)
                            .fillMaxWidth(),
                        text = words[it].value,
                        fontSize = FontSize.H6,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(Padding.large))
        }
    }
}

@Composable
fun FullScreenLoading() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenLoadingPreview() {
    FullScreenLoading()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenContentPreview() {
    HomeScreenPostsContent(
        listOf(
            WordModel(value = "kolonne", id = 986, guessed = false),
            WordModel(value = "molekyle", id = 987, guessed = false),
            WordModel(value = "vælg", id = 988, guessed = false),
            WordModel(value = "forkert", id = 989, guessed = false),
            WordModel(value = "grå", id = 990, guessed = false),
        ),
        listOf(
            WordModel(value = "column", id = 991, guessed = false),
            WordModel(value = "molecule", id = 992, guessed = false),
            WordModel(value = "select", id = 993, guessed = false),
            WordModel(value = "wrong", id = 994, guessed = false),
            WordModel(value = "gray", id = 996, guessed = false),
        )
    )
}