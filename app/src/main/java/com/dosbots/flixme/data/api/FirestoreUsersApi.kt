package com.dosbots.flixme.data.api

import com.dosbots.flixme.data.models.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUsersApi @Inject constructor() : UsersApi {

    private val firestore = Firebase.firestore

    override suspend fun fetchUser(id: String): User? {
        val a = firestore.collection("users")
            .document(id)
            .get()
            .await()
        return a.toObject(User::class.java)
    }

    override suspend fun createUser(user: User) {
        firestore.collection("users")
            .document(user.id)
            .set(user)
            .await()
    }
}