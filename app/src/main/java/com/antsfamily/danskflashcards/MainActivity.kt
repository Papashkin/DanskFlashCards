package com.antsfamily.danskflashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.antsfamily.danskflashcards.navigation.Navigator
import com.antsfamily.danskflashcards.ui.theme.DanskFlashCardsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DanskFlashCardsTheme {
                Navigator()
            }
        }
    }
}
