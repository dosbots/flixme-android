package com.dosbots.flixme.data.api.users

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
        return a.toObject(FirestoreUser::class.java)?.toApplicationModel()
    }

    override suspend fun createUser(user: User) {
        firestore.collection("users")
            .document(user.id)
            .set(user)
            .await()
    }
}

private data class FirestoreUser(
    val id: String? = null,
    val name: String? = null
)

private fun FirestoreUser.toApplicationModel(): User {
    return User(id.orEmpty(), name.orEmpty())
}