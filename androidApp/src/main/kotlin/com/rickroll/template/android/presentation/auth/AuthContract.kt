package com.rickroll.template.android.presentation.auth

import com.rickroll.template.auth.domain.model.AuthMode

data class AuthState(
    val mode: AuthMode = AuthMode.LOGIN,
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface AuthEvent {
    data class LoginChanged(val value: String) : AuthEvent
    data class PasswordChanged(val value: String) : AuthEvent
    data class ModeChanged(val value: AuthMode) : AuthEvent
    data object Submit : AuthEvent
    data object ErrorConsumed : AuthEvent
}

sealed interface AuthEffect {
    data class AuthSucceeded(val mode: AuthMode) : AuthEffect
}
