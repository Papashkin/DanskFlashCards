package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.core.util.toOrdinal
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.wistful_1000
import com.antsfamily.danskflashcards.ui.theme.wistful_300
import com.antsfamily.danskflashcards.ui.theme.wistful_50
import com.antsfamily.danskflashcards.ui.theme.wistful_700

@Composable
fun CurrentUserCard(
    modifier: Modifier = Modifier,
    score: Int,
    cardsSize: Int,
    place: Int?,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = wistful_50,
            contentColor = wistful_1000,
        ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$score/$cardsSize",
                        fontSize = FontSize.H6,
                        textAlign = TextAlign.Center,
                    )
                }
                Text(
                    modifier = Modifier.padding(top = Padding.small),
                    text = "My result",
                    color = wistful_700,
                    fontSize = FontSize.Body2,
                    textAlign = TextAlign.Center,
                )
            }
            VerticalDivider(
                Modifier.width(1.dp).padding(vertical = Padding.small),
                color = wistful_300
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = place?.toOrdinal() ?: "-",
                        fontSize = FontSize.H6,
                        textAlign = TextAlign.Center,
                    )
                }
                Text(
                    modifier = Modifier.padding(top = Padding.small),
                    text = "place",
                    color = wistful_700,
                    fontSize = FontSize.Body2,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CurrentUserCardPreview() {
    CurrentUserCard(score = 65, cardsSize = 982, place = 12)
}