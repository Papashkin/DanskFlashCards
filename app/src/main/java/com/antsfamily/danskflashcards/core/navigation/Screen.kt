package com.antsfamily.danskflashcards.core.navigation

import kotlinx.serialization.Serializable

sealed class Screen

@Serializable
object Splash: Screen()

@Serializable
object Auth: Screen()

@Serializable
data class Home(val id: String, val name: String, val email: String): Screen()

@Serializable
object Game: Screen()