package com.dosbots.flixme.di

import com.dosbots.flixme.data.authentication.AuthenticationMethod
import com.dosbots.flixme.data.authentication.AuthenticationStatus
import com.dosbots.flixme.data.authentication.DevelopmentAuthentication
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class AuthenticationModule {

    @Binds
    abstract fun bindDeveloperAuthenticationMethod(
        authentication: DevelopmentAuthentication
    ): AuthenticationMethod

    @Binds
    abstract fun bindDeveloperAuthenticationStatus(
        authentication: DevelopmentAuthentication
    ): AuthenticationStatus
}