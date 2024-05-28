package com.antsfamily.danskflashcards.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Auth : Screen("auth")
    data object Home : Screen("home/{username}/{userId}")
    data object Game : Screen("game")
}

private const val ROUTE_DELIMITER = "/"

fun Screen.toNavigationRoute(vararg argument: String): String {
    val arguments = argument.joinToString("/")
    return "${route.substringBefore(ROUTE_DELIMITER)}/$arguments"
}