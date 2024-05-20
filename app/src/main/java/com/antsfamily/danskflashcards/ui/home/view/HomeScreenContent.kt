package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.antsfamily.danskflashcards.data.WordModel
import com.antsfamily.danskflashcards.ui.home.HomeUiState
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun HomeScreenContent(
    content: HomeUiState.Content,
    onDanishWordClick: (WordModel) -> Unit,
    onEnglishWordClick: (WordModel) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Padding.small),
            modifier = Modifier.align(Alignment.Center)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(Padding.small),
                verticalArrangement = Arrangement.spacedBy(Padding.small)
            ) {
                items(content.danish.size) {
                    WordCard(content.status, content.danish[it]) { model ->
                        onDanishWordClick(model)
                    }
                    Spacer(modifier = Modifier.height(Padding.small))
                }
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(Padding.small),
                verticalArrangement = Arrangement.spacedBy(Padding.small)
            ) {
                items(content.english.size) {
                    WordCard(content.status, content.english[it]) { model ->
                        onEnglishWordClick(model)
                    }
                    Spacer(modifier = Modifier.height(Padding.small))
                }
            }
        }
    }
}