package com.antsfamily.danskflashcards.navigation

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object Home : Screen("home/{username}")
    data object Game : Screen("game")
}

private const val ROUTE_DELIMITER = "/"

fun Screen.toNavigationRoute(argument: String? = null): String {
    return argument?.let {
        "${route.substringBefore(ROUTE_DELIMITER)}/$argument"
    } ?: route
}