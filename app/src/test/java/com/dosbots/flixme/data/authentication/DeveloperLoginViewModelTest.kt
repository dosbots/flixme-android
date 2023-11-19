package com.dosbots.flixme.data.authentication

import com.dosbots.flixme.R
import com.dosbots.flixme.data.authentication.utils.FakeAuthenticationRepository
import com.dosbots.flixme.data.models.User
import com.dosbots.flixme.data.repository.AuthenticationRepository
import com.dosbots.flixme.ui.communication.UiMessage
import com.dosbots.flixme.ui.screens.login.DeveloperLoginState
import com.dosbots.flixme.ui.screens.login.DeveloperLoginViewModel
import com.dosbots.flixme.ui.screens.login.Event
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class DeveloperLoginViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When the ViewModel is initialized, then it emits the event OnScreenLoaded`() {
        val viewModel = newTestSubject()
        assertThat(viewModel.state.value).isEqualTo(DeveloperLoginState(event = Event.OnScreenLoaded))
    }

    @Test
    fun `When the ViewModel clearEvent method is called, then it emits a new state but with a null event`() {
        val viewModel = newTestSubject()

        // first assert that indeed there is an event
        assertThat(viewModel.state.value)
            .isEqualTo(DeveloperLoginState(event = Event.OnScreenLoaded))

        viewModel.clearEvent()

        assertThat(viewModel.state.value).isEqualTo(DeveloperLoginState(event = null))
    }

    @Test
    fun `When trying to login with an empty email, then the state is updated with an error message`() {
        assertCredentialsAreEmpty("", "pasword1")
    }

    @Test
    fun `When trying to login with an empty password, then the state is updated with an error message`() {
        assertCredentialsAreEmpty("email1", "")
    }

    @Test
    fun `When trying to login with both email and password empty, then the state is updated with an error message`() {
        assertCredentialsAreEmpty(email = "", password = "")
    }

    @Test
    fun `When trying to login with valid credentials, then the state is updated to show loading`() = runTest {
        val viewModel = newTestSubject()

        // remove the initial event
        viewModel.clearEvent()

        viewModel.signIn("contacto@dosbots.com", "mysupersecurepassword1")

        assertThat(viewModel.state.value).isEqualTo(DeveloperLoginState(loading = true))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When login in successfully, then the state is updated with OnSignInSuccess event`() = runTest {
        val viewModel = newTestSubject(
            FakeAuthenticationRepository {
                AuthenticationResult.Success(
                    isNew = true,
                    user = User(id = "myuid", name = "Batman")
                )
            }
        )

        // remove the initial event
        viewModel.clearEvent()

        viewModel.signIn("contacto@dosbots.com", "mysupersecurepassword1")

        advanceUntilIdle()

        assertThat(viewModel.state.value)
            .isEqualTo(DeveloperLoginState(event = Event.OnSignInSuccess))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When logging in with a disabled, then the state is updated with UserDisabled event`() = runTest {
        val viewModel = newTestSubject(
            FakeAuthenticationRepository { AuthenticationResult.UserDisabled }
        )

        // remove the initial event
        viewModel.clearEvent()

        viewModel.signIn("disabledUser@dosbots.com", "mysupersecurepassword1")

        advanceUntilIdle()

        assertThat(viewModel.state.value)
            .isEqualTo(DeveloperLoginState(event = Event.OnUserDisabled))
    }

    @Test
    fun `When the auth result is UserDeleted, UserTokenExpired, or UnknownError, then the state is updated with an error message`() = runTest {
        assertErrorAfterLogin(AuthenticationResult.UserDeleted)
        assertErrorAfterLogin(AuthenticationResult.UserTokenExpired)
        assertErrorAfterLogin(AuthenticationResult.UnknownError)
    }

    private fun assertCredentialsAreEmpty(email: String, password: String) {
        val viewModel = newTestSubject()

        viewModel.signIn(email, password)

        val expectedState = DeveloperLoginState(
            errorMessage = UiMessage(R.string.login_screen_email_or_password_empty_error)
        )
        assertThat(viewModel.state.value).isEqualTo(expectedState)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun assertErrorAfterLogin(authResult: AuthenticationResult) = runTest {
        val viewModel = newTestSubject(
            FakeAuthenticationRepository { authResult }
        )

        // remove the initial event
        viewModel.clearEvent()

        viewModel.signIn("someuser@dosbots.com", "mysupersecurepassword1")

        advanceUntilIdle()

        assertThat(viewModel.state.value)
            .isEqualTo(DeveloperLoginState(errorMessage = UiMessage(R.string.login_screen_generic_error)))
    }

    private fun newTestSubject(authenticationRepository: AuthenticationRepository? = null): DeveloperLoginViewModel {
        val repository = authenticationRepository  ?: FakeAuthenticationRepository { AuthenticationResult.UserDeleted }
        return DeveloperLoginViewModel(repository)
    }
}