package com.antsfamily.danskflashcards.ui.game.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.ui.game.model.WordItem
import com.antsfamily.danskflashcards.ui.game.model.mapToContainerColor
import com.antsfamily.danskflashcards.ui.game.model.mapToTextColor
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.wistful_100
import com.antsfamily.danskflashcards.ui.theme.wistful_1000
import com.antsfamily.danskflashcards.ui.theme.wistful_200

@Composable
fun WordCard(word: WordItem, onClick: (WordItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = word.isSelected,
                enabled = !word.isGuessed,
                role = null,
                onClick = {
                    onClick(word)
                }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = word.mapToContainerColor(),
            contentColor = wistful_1000,
            disabledContainerColor = wistful_100,
            disabledContentColor = wistful_200
        ),
    ) {
        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = Padding.small),
                text = word.value,
                fontSize = FontSize.Body1,
                fontWeight = FontWeight.SemiBold,
                color = word.mapToTextColor(),
                minLines = 1,
                maxLines = 1,
                textAlign = TextAlign.Center,
                style = if (word.isGuessed) {
                    TextStyle(textDecoration = TextDecoration.LineThrough)
                } else LocalTextStyle.current
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StartedWordCardPreview1() {
    Column(Modifier.padding(Padding.small)) {
        WordCard(
            word = WordItem(1, "to take care of", false, false, false),
            onClick = {}
        )
        Spacer(Modifier.height(10.dp))
        WordCard(
            word = WordItem(1, "to take care of", true, false, false),
            onClick = {}
        )
        Spacer(Modifier.height(10.dp))
        WordCard(
            word = WordItem(1, "to take care of", false, false, true),
            onClick = {}
        )
        Spacer(Modifier.height(10.dp))
        WordCard(
            word = WordItem(1, "to take care of", false, true, false),
            onClick = {}
        )
    }
}
