package com.antsfamily.danskflashcards.ui.home.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.ui.theme.leaderboard_bronze
import com.antsfamily.danskflashcards.ui.theme.leaderboard_gold
import com.antsfamily.danskflashcards.ui.theme.leaderboard_silver
import com.antsfamily.danskflashcards.ui.theme.light_accent
import com.antsfamily.danskflashcards.ui.theme.wistful_1000
import com.antsfamily.danskflashcards.ui.theme.wistful_500

data class LeaderboardItem(
    val name: String,
    val index: Int,
    val score: Int,
) {
    val modifiedName: String
        get() {
            return name.take(3).plus("*****").plus(name.last())
        }

    val place: Int
        get() = (index + 1)
}

fun LeaderboardItem.mapToTextColor(): Color {
    return if (index <= 2) {
        wistful_1000
    } else {
        wistful_500
    }
}

@DrawableRes
fun LeaderboardItem.mapToIcon(): Int? {
    return when (index) {
        0 -> R.drawable.ic_leaderboard_medal_gold
        1 -> R.drawable.ic_leaderboard_medal_silver
        2 -> R.drawable.ic_leaderboard_medal_bronze
        else -> null
    }
}

fun LeaderboardItem.mapToColor() = when (index) {
    0 -> leaderboard_gold
    1 -> leaderboard_silver
    2 -> leaderboard_bronze
    else -> light_accent
}
