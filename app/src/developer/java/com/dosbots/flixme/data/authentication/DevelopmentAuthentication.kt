package com.dosbots.flixme.data.authentication

import com.dosbots.flixme.data.authentication.firebase.FIREBASE_AUTH_EXCEPTION_INVALID_USER_TOKEN
import com.dosbots.flixme.data.authentication.firebase.FIREBASE_AUTH_EXCEPTION_USER_DISABLED
import com.dosbots.flixme.data.authentication.firebase.FIREBASE_AUTH_EXCEPTION_USER_NOT_FOUND
import com.dosbots.flixme.data.authentication.firebase.FIREBASE_AUTH_EXCEPTION_USER_TOKEN_EXPIRED
import com.dosbots.flixme.data.authentication.firebase.FirebaseAuthenticationMethod
import com.dosbots.flixme.data.models.User
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DevelopmentAuthentication @Inject constructor() : FirebaseAuthenticationMethod() {

    override suspend fun loginWithCredentials(credentials: FlixmeCredentials): AuthenticationResult {
        if (credentials !is DevelopmentCredentials) {
            throw IllegalArgumentException("Unknown credentials: $credentials")
        }
        return try {
            val authResult = signIn(credentials.email, credentials.password)

            val userId = authResult.user!!.uid
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            AuthenticationResult.Success(
                userId = userId,
                userData = User(
                    id = userId,
                    name = authResult.additionalUserInfo?.username ?: credentials.email
                ).takeIf { isNewUser },
                isNew = isNewUser
            )
        } catch (ex: FirebaseAuthInvalidUserException) {
            when (ex.errorCode) {
                FIREBASE_AUTH_EXCEPTION_USER_DISABLED -> {
                    AuthenticationResult.UserDisabled
                }
                FIREBASE_AUTH_EXCEPTION_USER_NOT_FOUND -> {
                    AuthenticationResult.UserDeleted
                }
                FIREBASE_AUTH_EXCEPTION_USER_TOKEN_EXPIRED,
                FIREBASE_AUTH_EXCEPTION_INVALID_USER_TOKEN -> {
                    AuthenticationResult.UserTokenExpired
                }
                else -> {
                    AuthenticationResult.UnknownError
                }
            }
        } catch (ex: Exception) {
            AuthenticationResult.UnknownError
        }
    }

    private suspend fun signIn(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
}