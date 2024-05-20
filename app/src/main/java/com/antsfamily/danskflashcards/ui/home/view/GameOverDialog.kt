package com.antsfamily.danskflashcards.ui.home.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.antsfamily.danskflashcards.data.DialogData

@Composable
fun GameOverDialog(data: DialogData, onDismiss: () -> Unit) {
    val resultText = if (data.isNewRecord) {
        "THAT'S YOUR NEW RECORD!!"
    } else {
        "Your best result is still ${data.bestResult}"
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Game is over")
        },
        text = {
            Text(text = "You guessed ${data.pairsAmount} pairs\n$resultText")
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(text = "OK")
            }
        }
    )
}