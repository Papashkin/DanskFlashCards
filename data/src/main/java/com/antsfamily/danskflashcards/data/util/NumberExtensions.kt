package com.antsfamily.danskflashcards.data.util

inline fun <reified T: Number> T?.orZero(): T = this ?: 0 as T