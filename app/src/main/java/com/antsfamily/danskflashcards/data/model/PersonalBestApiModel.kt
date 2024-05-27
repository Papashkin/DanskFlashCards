package com.antsfamily.danskflashcards.data.model

import com.antsfamily.danskflashcards.ui.home.model.PersonalBest
import com.antsfamily.danskflashcards.util.orZero
import java.math.BigDecimal
import java.math.RoundingMode

data class PersonalBestApiModel(
    val value: Int,
    val date: String
)

fun PersonalBestApiModel?.mapToModel(wordsAmount: Int): PersonalBest {
    val percent = BigDecimal
        .valueOf((this?.value.orZero() * 100L) / wordsAmount)
        .setScale(1, RoundingMode.HALF_DOWN)
    return PersonalBest(
        value = this?.value.orZero(),
        percent = percent,
        date = this?.date.orEmpty()
    )
}