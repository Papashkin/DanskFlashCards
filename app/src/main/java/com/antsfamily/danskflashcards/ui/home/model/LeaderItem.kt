package com.antsfamily.danskflashcards.ui.home.model

private const val DOT = "."
const val SEPARATOR_SPACE = " "

data class LeaderItem(
    val name: String,
    val surname: String?,
    val index: Int,
    val score: Int,
    val isUser: Boolean
) {
    val modifiedName: String
        get() {
            return if (surname.isNullOrBlank()) {
                name
            } else {
                val username = listOf(name, surname.first().plus(DOT))
                username.joinToString(separator = SEPARATOR_SPACE)
            }
        }

    val place: Int
        get() = (index + 1)
}
