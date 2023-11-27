package com.dosbots.flixme.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dosbots.flixme.R
import com.dosbots.flixme.data.authentication.AuthenticationResult
import com.dosbots.flixme.data.authentication.DevelopmentCredentials
import com.dosbots.flixme.data.repository.AuthenticationRepository
import com.dosbots.flixme.ui.communication.UiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeveloperLoginViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DeveloperLoginState(event = Event.OnScreenLoaded))
    val state = _state.asStateFlow()

    fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _state.value = DeveloperLoginState(
                errorMessage = UiMessage(R.string.login_screen_email_or_password_empty_error)
            )
            return
        }

        _state.update { it.copy(loading = true) }

        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                // TODO log authentication error
                _state.value = DeveloperLoginState(
                    errorMessage = UiMessage(R.string.login_screen_generic_error)
                )
            }
        ) {
            val signInResult = authenticationRepository.signIn(DevelopmentCredentials(email, password))
            when (signInResult) {
                is AuthenticationResult.Success -> {
                    _state.value = DeveloperLoginState(
                        event = Event.OnSignInSuccess
                    )
                }
                AuthenticationResult.UserDisabled -> {
                    _state.value = DeveloperLoginState(
                        event = Event.OnUserDisabled
                    )
                }
                else -> {
                    _state.value = DeveloperLoginState(
                        errorMessage = UiMessage(R.string.login_screen_generic_error)
                    )
                }
            }
        }
    }

    fun clearErrorMessage() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun clearEvent() {
        _state.update { it.copy(event = null) }
    }
}