package com.antsfamily.danskflashcards.core.util

import java.util.Locale

fun Long.toTimeFormat(): String {
    val minutes = this / 60
    val remainingSeconds = this % 60

    return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
}

inline fun <reified T: Number> T?.orZero(): T = this ?: 0 as T

fun Int.toOrdinal(): String {
    if (this in 11..13) {
        return "${this}th"
    }

    return when (this % 10) {
        1 -> "${this}st"
        2 -> "${this}nd"
        3 -> "${this}rd"
        else -> "${this}th"
    }
}