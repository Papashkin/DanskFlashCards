package com.antsfamily.danskflashcards.ui.game.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.data.DialogData
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.wistful_50

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameOverDialog(
    data: DialogData,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        confirmValueChange = { false },
    )

    val resultText = if (data.isNewRecord) {
        stringResource(R.string.new_record)
    } else {
        stringResource(R.string.your_best_result, data.bestResult)
    }
    ModalBottomSheet(
        modifier = Modifier.height(400.dp),
        onDismissRequest = onDismiss,
        containerColor = wistful_50,
        sheetState = modalBottomSheetState,
        shape = BottomSheetCornerShape(Padding.large)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .padding(top = Padding.small)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Padding.medium),
                    text = stringResource(id = R.string.game_over),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Padding.medium, top = Padding.xLarge),
                    text = stringResource(
                        R.string.game_over_dialog_description, data.pairsAmount, resultText
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Padding.medium, vertical = Padding.large),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(Padding.large),
                    onClick = onDismiss
                ) {
                    Text(text = stringResource(R.string.button_ok))
                }
            }
        }
    }
}

fun BottomSheetCornerShape(cornerRadius: Dp = 0.dp) =
    RoundedCornerShape(cornerRadius, cornerRadius, 0.dp, 0.dp)