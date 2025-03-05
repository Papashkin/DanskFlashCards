package com.antsfamily.danskflashcards.util

import com.antsfamily.danskflashcards.core.navigation.Home
import com.antsfamily.danskflashcards.ui.auth.CurrentUserModel

fun Home.toModel(): CurrentUserModel {
    return CurrentUserModel(username = this.name, userId = this.id, email = this.email)
}