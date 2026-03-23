package com.rickroll.template.android.presentation.auth

import androidx.lifecycle.viewModelScope
import com.rickroll.template.android.presentation.mvi.MviViewModel
import com.rickroll.template.auth.domain.interactor.LoginInteractor
import com.rickroll.template.auth.domain.interactor.RegisterInteractor
import com.rickroll.template.auth.domain.model.AuthMode
import com.rickroll.template.core.result.RequestErrorCode
import com.rickroll.template.core.result.RequestResult
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginInteractor: LoginInteractor,
    private val registerInteractor: RegisterInteractor,
) : MviViewModel<AuthState, AuthEvent, AuthEffect>(AuthState()) {

    override fun handleEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.LoginChanged -> setState { copy(login = event.value) }
            is AuthEvent.PasswordChanged -> setState { copy(password = event.value) }
            is AuthEvent.ModeChanged -> setState { copy(mode = event.value, errorMessage = null) }
            AuthEvent.ErrorConsumed -> setState { copy(errorMessage = null) }
            AuthEvent.Submit -> submit()
        }
    }

    private fun submit() {
        withState { current ->
            viewModelScope.launch {
                setState { copy(isLoading = true, errorMessage = null) }

                val result = when (current.mode) {
                    AuthMode.LOGIN -> loginInteractor(current.login, current.password)
                    AuthMode.REGISTER -> registerInteractor(current.login, current.password)
                }

                when (result) {
                    is RequestResult.Success -> {
                        setState { copy(isLoading = false) }
                        sendEffect(AuthEffect.AuthSucceeded(current.mode))
                    }

                    is RequestResult.Error -> {
                        setState {
                            copy(
                                isLoading = false,
                                errorMessage = result.code.toUiMessage(),
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun RequestErrorCode.toUiMessage(): String = when (this) {
    RequestErrorCode.EMPTY_LOGIN -> "Введите логин"
    RequestErrorCode.EMPTY_PASSWORD -> "Введите пароль"
    RequestErrorCode.LOGIN_ALREADY_EXISTS -> "Пользователь уже существует"
    RequestErrorCode.INVALID_CREDENTIALS -> "Неверный логин или пароль"
    RequestErrorCode.DATABASE,
    RequestErrorCode.UNKNOWN,
    -> "Ошибка авторизации"
}
