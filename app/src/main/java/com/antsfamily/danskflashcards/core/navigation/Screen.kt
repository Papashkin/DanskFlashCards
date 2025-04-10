package com.antsfamily.danskflashcards.core.navigation

import kotlinx.serialization.Serializable

sealed class Screen

@Serializable
data object Splash: Screen()

@Serializable
data object Auth: Screen()

@Serializable
data object Settings: Screen()

@Serializable
data class Onboarding1(val id: String, val name: String): Screen()

@Serializable
data class Onboarding2(val id: String, val name: String): Screen()

@Serializable
data class Home(val id: String, val name: String): Screen()

@Serializable
data class Game(val id: String, val score: Int): Screen()

@Serializable
data object Leaderboard: Screen()