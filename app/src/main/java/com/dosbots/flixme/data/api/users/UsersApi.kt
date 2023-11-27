package com.dosbots.flixme.data.api.users

import com.dosbots.flixme.data.models.User

interface UsersApi {
    suspend fun fetchUser(id: String): User?
    suspend fun createUser(user: User)
}