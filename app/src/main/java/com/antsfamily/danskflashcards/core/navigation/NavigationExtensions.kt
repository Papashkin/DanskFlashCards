package com.antsfamily.danskflashcards.core.navigation

import com.antsfamily.danskflashcards.data.UserData

fun Home.toUsedData(): UserData {
    return UserData(username = this.name, userId = this.id, email = this.email)
}