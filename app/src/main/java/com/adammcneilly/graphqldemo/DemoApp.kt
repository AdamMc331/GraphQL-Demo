package com.adammcneilly.graphqldemo

import android.app.Application
import com.apollographql.apollo.ApolloClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class DemoApp : Application() {
    val baseUrl: String
        get() = "https://api.github.com/graphql"

    val apolloClient: ApolloClient
        get() {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val authInterceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "bearer ${BuildConfig.GITHUB_OAUTH_TOKEN}")
                    .build()

                chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

            return ApolloClient.builder()
                .serverUrl(baseUrl)
                .okHttpClient(okHttpClient)
                .build()
        }
}