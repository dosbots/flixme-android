package com.dosbots.flixme.ui.screens.login

import com.dosbots.flixme.ui.communication.UiMessage

data class DeveloperLoginState(
    val loading: Boolean = false,
    val errorMessage: UiMessage? = null,
    val event: Event? = null
)

sealed class Event {
    data object OnScreenLoaded : Event()
    data object OnSignInSuccess : Event()
    data object OnUserDisabled : Event()
}