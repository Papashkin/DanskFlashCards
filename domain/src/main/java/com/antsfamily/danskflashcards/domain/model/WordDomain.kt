package com.antsfamily.danskflashcards.domain.model

import com.antsfamily.danskflashcards.data.model.WordApiModel

data class WordDomain(
    val id: Int,
    val type: LanguageType,
    val value: String,
)

fun WordApiModel?.mapToDomain(type: LanguageType): WordDomain? {
    if (this == null) return null

    return WordDomain(
        id = this.id,
        type = type,
        value = when (type) {
            LanguageType.DK -> this.danish
            LanguageType.DE -> this.german
            LanguageType.EN -> this.english
            LanguageType.RU -> this.russian
        },
    )
}
