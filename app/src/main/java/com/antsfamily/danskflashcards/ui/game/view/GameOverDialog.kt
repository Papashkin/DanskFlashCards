package com.antsfamily.danskflashcards.ui.game.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.ui.game.model.GameOverModel
import com.antsfamily.danskflashcards.ui.theme.Padding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameOverDialog(
    data: GameOverModel,
    sheetState: SheetState = rememberModalBottomSheetState(
        confirmValueChange = { false },
    ),
    onDismiss: () -> Unit
) {
    val resultText = if (data.isNewRecord) {
        stringResource(R.string.new_record)
    } else {
        stringResource(R.string.your_best_result, data.bestResult)
    }
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        shape = RoundedCornerShape(Padding.large, Padding.large, 0.dp, 0.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(horizontal = Padding.medium),
                text = stringResource(id = R.string.game_over),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Padding.large),
                text = stringResource(
                    R.string.game_over_dialog_description, data.newResult, resultText
                ),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .width(300.dp)
                    .padding(horizontal = Padding.small, vertical = Padding.large),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(Padding.large),
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(R.string.button_exit),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameOverDialogPreview(modifier: Modifier = Modifier) {
    GameOverDialog(GameOverModel()) {}
}

@Preview
@Composable
fun ButtonPreview(modifier: Modifier = Modifier) {
    Button(
        modifier = Modifier.width(200.dp),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(Padding.large),
        onClick = { }
    ) {
        Text(
            text = stringResource(R.string.button_exit),
            style = MaterialTheme.typography.titleLarge
        )
    }
}