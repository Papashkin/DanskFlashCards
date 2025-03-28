package com.antsfamily.danskflashcards.core.util

import com.antsfamily.danskflashcards.core.navigation.Home
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.navigation.Onboarding

fun Home.toModel(): CurrentUserItem {
    return CurrentUserItem(username = name, userId = id)
}

fun Onboarding.toModel(): CurrentUserItem {
    return CurrentUserItem(username = name, userId = id)
}