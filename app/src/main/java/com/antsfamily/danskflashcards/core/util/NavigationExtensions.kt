package com.antsfamily.danskflashcards.core.util

import com.antsfamily.danskflashcards.core.navigation.Home
import com.antsfamily.danskflashcards.core.model.CurrentUserItem

fun Home.toModel(): CurrentUserItem {
    return CurrentUserItem(username = name, userId = id, email = email)
}