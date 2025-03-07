package com.antsfamily.danskflashcards.core.util

import com.antsfamily.danskflashcards.core.navigation.Home
import com.antsfamily.danskflashcards.ui.auth.CurrentUserModel

fun Home.toModel(): CurrentUserModel {
    return CurrentUserModel(username = name, userId = id, email = email)
}