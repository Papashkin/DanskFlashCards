package com.antsfamily.danskflashcards.ui.home.model

data class LeaderboardModel(
    val leaders: List<LeaderboardItem>,
    val user: LeaderboardItem,
)