package com.dosbots.flixme.data.di

import com.dosbots.flixme.BuildConfig
import com.dosbots.flixme.data.api.movies.interceptor.MoviesApiInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
class NetworkingModule {

    @Provides
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder().also { okHttpClient ->
            if (BuildConfig.DEBUG) {
                okHttpClient.addInterceptor(
                    HttpLoggingInterceptor().also { httpLoggingInterceptor ->
                        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }
        }
    }

    @Named("movies_api_okhttp_client_builder")
    @Provides
    fun provideOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder): OkHttpClient {
        return okHttpClientBuilder
            .addInterceptor(MoviesApiInterceptor(BuildConfig.MOVIES_API_KEY))
            .build()
    }

    @Provides
    fun provideBaseRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
    }
}
