package com.antsfamily.danskflashcards.data.source.remote

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CacheInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val cacheControl = CacheControl.Builder()
            .maxStale(1, TimeUnit.DAYS)
            .build()

        request = request.newBuilder()
            .cacheControl(cacheControl)
            .build()
        val response = chain.proceed(request)

        return response.newBuilder()
            .removeHeader("Cache-Control")
            .header("Cache-Control", "public, max-age=86400")
            .build()
    }
}