package com.antsfamily.danskflashcards.core.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private const val DATE_PATTERN = "dd/MM/yyyy, HH:mm"

fun Timestamp.toIsoString(): String {
    val date = this.toDate()
    val formatter = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(date)
}