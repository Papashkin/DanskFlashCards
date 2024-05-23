package com.antsfamily.danskflashcards.navigation

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object Home : Screen("home/{username}")
    data object Game : Screen("game")
}