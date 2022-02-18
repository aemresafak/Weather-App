package com.example.weatherprojecttry_1.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
private const val ACCESS_KEY = "02bcea2ebb2f23b9542aedfd532a82fa"

/**
 * Adds an access_key query to the existing url with parameter ACCESS_KEY defined in this kotlin file.
 */
class KeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request().url()
            .newBuilder()
            .addQueryParameter("access_key",ACCESS_KEY)
            .build()
        return chain.proceed(
            chain.request()
                .newBuilder()
                .url(newUrl)
                .build()
        )

    }
}