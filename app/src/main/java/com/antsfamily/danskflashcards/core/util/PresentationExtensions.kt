package com.antsfamily.danskflashcards.core.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.domain.model.LanguageType
import com.antsfamily.danskflashcards.ui.home.model.LeaderItem
import com.antsfamily.danskflashcards.ui.theme.leaderboard_bronze
import com.antsfamily.danskflashcards.ui.theme.leaderboard_gold
import com.antsfamily.danskflashcards.ui.theme.leaderboard_silver
import com.antsfamily.danskflashcards.ui.theme.light_accent
import com.antsfamily.danskflashcards.ui.theme.wistful_1000

@StringRes
fun LanguageType.toStringRes(): Int {
    return when (this) {
        LanguageType.DK -> R.string.language_dk
        LanguageType.DE -> R.string.language_de
        LanguageType.EN -> R.string.language_en
        LanguageType.RU -> R.string.language_ru
    }
}

@DrawableRes
fun LanguageType.toFlagIconRes(): Int {
    return when (this) {
        LanguageType.DK -> R.drawable.ic_flag_dk
        LanguageType.DE -> R.drawable.ic_flag_de
        LanguageType.EN -> R.drawable.ic_flag_en
        LanguageType.RU -> R.drawable.ic_flag_ru
    }
}

@StringRes
fun ErrorType.toErrorMessage(): Int {
    return when (this) {
        ErrorType.NetworkConnection -> R.string.error_text_network
        ErrorType.Server -> R.string.error_text_server
        ErrorType.Unknown -> R.string.error_text_unknown
    }
}

fun LeaderItem.mapToTextColor(): Color {
    return if (isUser) light_accent else wistful_1000
}

@DrawableRes
fun LeaderItem.mapToIcon(): Int? {
    return when (index) {
        0 -> R.drawable.ic_leaderboard_medal_gold
        1 -> R.drawable.ic_leaderboard_medal_silver
        2 -> R.drawable.ic_leaderboard_medal_bronze
        else -> null
    }
}

fun LeaderItem.mapToColor() = when (index) {
    0 -> leaderboard_gold
    1 -> leaderboard_silver
    2 -> leaderboard_bronze
    else -> if (isUser) light_accent else wistful_1000
}