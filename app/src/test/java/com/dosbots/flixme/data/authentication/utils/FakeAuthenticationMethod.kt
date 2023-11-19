package com.dosbots.flixme.data.authentication.utils

import com.dosbots.flixme.data.authentication.AuthenticationMethod
import com.dosbots.flixme.data.authentication.AuthenticationResult
import com.dosbots.flixme.data.authentication.FlixmeCredentials

class FakeAuthenticationMethod(
    testUserId: String = "",
    isUserLogged: Boolean = false,
    private val onLoginWithCredentialsCalled: (FlixmeCredentials) -> AuthenticationResult
) : AuthenticationMethod {

    override val userId: String = testUserId

    override val isUserLogged: Boolean = isUserLogged

    override suspend fun loginWithCredentials(credentials: FlixmeCredentials): AuthenticationResult {
        return onLoginWithCredentialsCalled(credentials)
    }
}