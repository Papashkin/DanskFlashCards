package com.antsfamily.danskflashcards.core.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.domain.model.LanguageType

@StringRes
fun LanguageType.toStringRes(): Int {
    return when (this) {
        LanguageType.DK -> R.string.language_dk
        LanguageType.DE -> R.string.language_de
        LanguageType.EN -> R.string.language_en
        LanguageType.RU -> R.string.language_ru
    }
}


fun LanguageType.toDisplayName(): String {
    return when (this) {
        LanguageType.DK -> "Danish"
        LanguageType.DE -> "Deutsch"
        LanguageType.EN -> "English"
        LanguageType.RU -> "Русский"
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