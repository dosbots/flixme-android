package com.dosbots.flixme.data.repository

import com.dosbots.flixme.data.api.users.UsersApi
import com.dosbots.flixme.data.authentication.AuthenticationMethod
import com.dosbots.flixme.data.authentication.AuthenticationResult
import com.dosbots.flixme.data.authentication.FlixmeCredentials
import com.dosbots.flixme.data.dabase.UsersDao
import com.dosbots.flixme.data.models.User
import javax.inject.Inject

interface AuthenticationRepository {
    suspend fun signIn(credentials: FlixmeCredentials): AuthenticationResult
}

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationMethod: AuthenticationMethod,
    private val usersDao: UsersDao,
    private val usersApi: UsersApi
) : AuthenticationRepository {

    override suspend fun signIn(credentials: FlixmeCredentials): AuthenticationResult {
        val result = authenticationMethod.loginWithCredentials(credentials)
        if (result is AuthenticationResult.Success) {
            val user: User = if (result.isNew && result.userData != null) {
                usersApi.createUser(result.userData)
                result.userData
            } else {
                usersApi.fetchUser(result.userId) ?: error("User not found")
            }
            usersDao.insert(user)
        }
        return result
    }
}
