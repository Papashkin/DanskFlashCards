package com.antsfamily.danskflashcards.util


fun Long.toTimeFormat(): String {
    val minutes = this / 60
    val remainingSeconds = this % 60

    return String.format("%02d:%02d", minutes, remainingSeconds)
}

inline fun <reified T: Number> T?.orZero(): T = this ?: 0 as T