package com.antsfamily.danskflashcards.domain.model

data class WordDomain(
    val id: Int,
    val danish : String,
    val german : String,
    val english : String,
    val russian : String,
)

fun WordDomain?.toSpecificLanguage(type: LanguageType): SpecificLanguageWordDomain? {
    if (this == null) return null

    return SpecificLanguageWordDomain(
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