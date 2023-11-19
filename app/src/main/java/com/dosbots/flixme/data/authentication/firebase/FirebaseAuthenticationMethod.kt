package com.dosbots.flixme.data.authentication.firebase

import com.dosbots.flixme.data.authentication.AuthenticationMethod
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

abstract class FirebaseAuthenticationMethod : AuthenticationMethod {

    protected val firebaseAuth = Firebase.auth

    override val userId: String
        get() = firebaseAuth.currentUser?.uid ?: error("User has not signed in")

    override val isUserLogged: Boolean
        get() = firebaseAuth.currentUser != null
}
