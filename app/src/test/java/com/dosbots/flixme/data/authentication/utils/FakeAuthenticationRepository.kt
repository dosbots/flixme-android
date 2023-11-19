package com.dosbots.flixme.data.authentication.utils

import com.dosbots.flixme.data.authentication.AuthenticationResult
import com.dosbots.flixme.data.authentication.FlixmeCredentials
import com.dosbots.flixme.data.repository.AuthenticationRepository

class FakeAuthenticationRepository(
    private val onSignInCalled: (FlixmeCredentials) -> AuthenticationResult
) : AuthenticationRepository {

    override suspend fun signIn(credentials: FlixmeCredentials): AuthenticationResult {
        return onSignInCalled(credentials)
    }
}
