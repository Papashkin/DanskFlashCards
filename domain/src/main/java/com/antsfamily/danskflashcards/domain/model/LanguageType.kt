package com.antsfamily.danskflashcards.domain.model

enum class LanguageType {
    DK,
    DE,
    EN,
    RU,
    ;

    fun isEnglish(): Boolean = this == EN
}