package com.antsfamily.danskflashcards.core.model

import androidx.annotation.StringRes
import com.antsfamily.danskflashcards.R
import java.net.UnknownHostException

enum class ErrorType {
    NetworkConnection,
    Server,
    Unknown,
    ;
}

fun Exception.mapToErrorType(): ErrorType {
    return when (this) {
        is UnknownHostException -> ErrorType.NetworkConnection
        else -> ErrorType.Unknown
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