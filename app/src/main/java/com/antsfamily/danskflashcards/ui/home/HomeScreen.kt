package com.antsfamily.danskflashcards.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    when (val stateValue = state.value) {
        HomeUiState.Loading -> FullScreenLoading()
        is HomeUiState.Content -> HomeScreenPostsContent(stateValue.content)
        is HomeUiState.Error -> {
            //TODO add error handler
        }
    }
}

@Composable
fun HomeScreenPostsContent(content: List<String>) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text(text = "Matching game", fontSize = 20.sp)
        Text(
            text = "Find pairs as fast as possible",
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 12.dp)
        )
        Spacer(modifier = Modifier.padding(top = 32.dp))
        Box(modifier = Modifier.fillMaxSize()) {
            // Align the content to the bottom of the Box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .align(Alignment.BottomCenter)
            ) {
                GridWithCards(content)
            }
        }
    }
}

@Composable
fun GridWithCards(items: List<String>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        content = {
            items(items.take(8)) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                        .height(80.dp)
                        .background(Color.LightGray)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    )
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
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test"
        )
    )
}