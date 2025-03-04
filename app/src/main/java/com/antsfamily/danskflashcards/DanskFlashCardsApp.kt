package com.antsfamily.danskflashcards

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DanskFlashCardsApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)
    }
}