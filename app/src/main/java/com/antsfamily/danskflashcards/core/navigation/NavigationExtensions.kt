package com.antsfamily.danskflashcards.core.navigation

import com.antsfamily.danskflashcards.ui.auth.CurrentUserModel

fun Home.toModel(): CurrentUserModel {
    return CurrentUserModel(username = this.name, userId = this.id, email = this.email)
}