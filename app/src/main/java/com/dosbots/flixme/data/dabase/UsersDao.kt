package com.dosbots.flixme.data.dabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dosbots.flixme.data.models.User

@Dao
interface UsersDao {

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getById(userId: String): User

    @Insert
    suspend fun insert(user: User)
}