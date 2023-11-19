package com.dosbots.flixme.data.authentication

import com.dosbots.flixme.data.models.User

sealed interface AuthenticationResult {
    data class Success(
        val user: User,
        val isNew: Boolean,
    ) : AuthenticationResult
    data object UserDeleted : AuthenticationResult
    data object UserDisabled : AuthenticationResult
    data object UserTokenExpired : AuthenticationResult
    data object UnknownError : AuthenticationResult
}