package com.antsfamily.danskflashcards.ui.game.model

import androidx.compose.ui.graphics.Color
import com.antsfamily.danskflashcards.domain.model.SpecificLanguageWordDomain
import com.antsfamily.danskflashcards.ui.theme.alert
import com.antsfamily.danskflashcards.ui.theme.wistful_0
import com.antsfamily.danskflashcards.ui.theme.wistful_100
import com.antsfamily.danskflashcards.ui.theme.wistful_1000
import com.antsfamily.danskflashcards.ui.theme.wistful_300
import com.antsfamily.danskflashcards.ui.theme.wistful_600

data class WordItem(
    val id: Int,
    val value: String,
    val isSelected: Boolean,
    val isGuessed: Boolean,
    val isWrong: Boolean,
)

fun WordItem.mapToContainerColor(): Color = when {
    isSelected && !isGuessed -> wistful_600
    isGuessed -> wistful_100
    isWrong -> alert
    else -> wistful_300
}

fun WordItem.mapToTextColor(): Color = when {
    isSelected && !isGuessed -> wistful_0
    isGuessed -> wistful_300
    isWrong -> wistful_0
    else -> wistful_1000
}

fun SpecificLanguageWordDomain.mapToItem(): WordItem {
    return WordItem(
        id = this.id,
        value = this.value,
        isSelected = false,
        isGuessed = false,
        isWrong = false,
    )
}
