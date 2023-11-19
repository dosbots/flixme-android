package com.dosbots.flixme.data.authentication

interface AuthenticationStatus {
    val isUserLogged: Boolean
}

interface AuthenticationMethod : AuthenticationStatus {

    val userId: String

    suspend fun loginWithCredentials(credentials: FlixmeCredentials): AuthenticationResult
}