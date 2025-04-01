package com.antsfamily.danskflashcards.core.util

import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.navigation.Home
import com.antsfamily.danskflashcards.core.navigation.Onboarding1
import com.antsfamily.danskflashcards.core.navigation.Onboarding2

fun Home.toModel(): CurrentUserItem {
    return CurrentUserItem(username = name, userId = id)
}

fun Onboarding1.toModel(): CurrentUserItem {
    return CurrentUserItem(username = name, userId = id)
}

fun Onboarding2.toModel(): CurrentUserItem {
    return CurrentUserItem(username = name, userId = id)
}