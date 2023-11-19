package com.dosbots.flixme.data.authentication

import com.dosbots.flixme.data.models.User

sealed interface AuthenticationResult {
    data class Success(
        val user: User,
        val isNew: Boolean,
    ) : AuthenticationResult
    object UserDeleted : AuthenticationResult
    object UserDisabled : AuthenticationResult
    object UserTokenExpired : AuthenticationResult
    object UnknownError : AuthenticationResult
}