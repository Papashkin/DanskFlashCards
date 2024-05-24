package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.data.PersonalBest
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.wistful_0
import com.antsfamily.danskflashcards.ui.theme.wistful_100
import com.antsfamily.danskflashcards.ui.theme.wistful_400
import com.antsfamily.danskflashcards.ui.theme.wistful_700
import com.antsfamily.danskflashcards.ui.theme.wistful_800
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun PersonalBestCard(data: PersonalBest, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(top = Padding.xLarge)
            .fillMaxWidth()
            .height(200.dp)
            .background(wistful_800, shape = RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(horizontal = Padding.medium)) {
            Row(modifier = Modifier.padding(vertical = Padding.large)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        modifier = Modifier.width(100.dp),
                        text = stringResource(R.string.personal_best),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = wistful_400
                    )
                    Text(
                        modifier = Modifier.width(100.dp),
                        text = data.value.toString(),
                        fontSize = FontSize.H3,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = wistful_100
                    )
                }
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Padding.small)
                ) {
                    Text(
                        text = stringResource(R.string.date_of_the_pb),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = wistful_400
                    )
                    Text(
                        text = data.date,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = wistful_100
                    )

                    Text(
                        modifier = Modifier.padding(top = Padding.medium),
                        text = stringResource(R.string.of_all_words),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = wistful_400
                    )
                    Text(
                        text = "${data.percent}%",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = wistful_100
                    )
                }
            }
            Divider(
                modifier = Modifier.padding(horizontal = Padding.large),
                thickness = 1.dp,
                color = wistful_700
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = wistful_0
                    ),
                    shape = RoundedCornerShape(Padding.xLarge),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(Padding.medium)
                        .width(100.dp),
                    contentPadding = PaddingValues(0.dp),
                    border = BorderStroke(1.dp, wistful_0)
                ) {
                    Text(
                        text = stringResource(id = R.string.start),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PersonalBestCardPreview() {
    val percent = BigDecimal(32.333434).setScale(1, RoundingMode.HALF_DOWN)
    PersonalBestCard(
        PersonalBest(45, percent, "yyyy/MM/dd HH:mm:ss")
    ) {}
}