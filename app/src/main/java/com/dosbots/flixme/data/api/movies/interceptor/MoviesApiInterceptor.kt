package com.dosbots.flixme.data.api.movies.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class MoviesApiInterceptor(private val moviesApiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter("api_key", moviesApiKey)
            .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}
