package com.dosbots.flixme.data.repository

import com.dosbots.flixme.data.api.UsersApi
import com.dosbots.flixme.data.authentication.AuthenticationResult
import com.dosbots.flixme.data.authentication.utils.FakeAuthenticationMethod
import com.dosbots.flixme.data.authentication.utils.FakeCredentials
import com.dosbots.flixme.data.dabase.UsersDao
import com.dosbots.flixme.data.models.User
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class AuthenticationRepositoryTest {

    @MockK
    private lateinit var usersApi: UsersApi

    @MockK
    private lateinit var usersDao: UsersDao

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When logging in is successful, and it is a new user, then make a request to create the user and insert it into the database`() = runTest {
        val user = User(id = "user-id-123", name = "John Lennon")
        val authenticationRepository = newTestSubject(
            expectedAuthResult = AuthenticationResult.Success(isNew = true, user = user)
        )

        val result = authenticationRepository.signIn(FakeCredentials)

        coVerifyOrder {
            usersApi.createUser(user)
            usersDao.insert(user)
        }
        assertThat(result).isInstanceOf(AuthenticationResult.Success::class.java)
    }

    @Test
    fun `When logging in is successful, but user is not new, then just insert it into the database`() = runTest {
        val user = User(id = "user-id-123", name = "John Lennon")
        val authenticationRepository = newTestSubject(
            expectedAuthResult = AuthenticationResult.Success(isNew = false, user = user)
        )

        val result = authenticationRepository.signIn(FakeCredentials)

        coVerify {
            usersDao.insert(user)
        }
        coVerify(exactly = 0) {
            usersApi.createUser(user)
        }
        assertThat(result).isInstanceOf(AuthenticationResult.Success::class.java)
    }

    @Test
    fun `When logging in is not successful, then just return the result`() = runTest {
        val user = User(id = "user-id-123", name = "John Lennon")
        val authenticationRepository = newTestSubject(
            expectedAuthResult = AuthenticationResult.UnknownError
        )

        val result = authenticationRepository.signIn(FakeCredentials)

        coVerify(exactly = 0) {
            usersDao.insert(user)
        }
        coVerify(exactly = 0) {
            usersApi.createUser(user)
        }
        assertThat(result).isEqualTo(AuthenticationResult.UnknownError)
    }


    private fun newTestSubject(expectedAuthResult: AuthenticationResult): AuthenticationRepository {
        return AuthenticationRepositoryImpl(
            authenticationMethod = FakeAuthenticationMethod { expectedAuthResult },
            usersDao,
            usersApi
        )
    }

}