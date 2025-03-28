package com.antsfamily.danskflashcards.data.source.remote

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class FirebaseAuthInterceptor @Inject constructor(
    private val firebaseHandler: FirebaseHandler
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { firebaseHandler.getUserToken() }

        val originalRequest = chain.request()

        return if (token != null) {
            val newRequest = originalRequest.withAuthToken(token)
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }

    private fun Request.withAuthToken(token: String): Request {
        val newUrl = this.url().newBuilder()
            .addQueryParameter("auth", token)
            .build()

        return this.newBuilder()
            .url(newUrl)
            .build()
    }
}