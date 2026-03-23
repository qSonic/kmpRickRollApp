package com.rickroll.template.auth.domain.service

import com.rickroll.template.auth.domain.interactor.GetSessionInteractor
import com.rickroll.template.auth.domain.interactor.LoginInteractor
import com.rickroll.template.auth.domain.interactor.LogoutInteractor
import com.rickroll.template.auth.domain.interactor.RegisterInteractor
import com.rickroll.template.auth.domain.model.SessionState
import com.rickroll.template.core.result.RequestErrorCode
import com.rickroll.template.core.result.RequestResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthService : KoinComponent {
    private val getSessionInteractor: GetSessionInteractor by inject()
    private val loginInteractor: LoginInteractor by inject()
    private val registerInteractor: RegisterInteractor by inject()
    private val logoutInteractor: LogoutInteractor by inject()

    fun isAuthorized(): Boolean {
        val result = getSessionInteractor()
        return result is RequestResult.Success && result.data is SessionState.Authorized
    }

    fun login(login: String, password: String): RequestErrorCode? =
        loginInteractor(login, password).errorCodeOrNull()

    fun register(login: String, password: String): RequestErrorCode? =
        registerInteractor(login, password).errorCodeOrNull()

    fun logout(): Boolean = logoutInteractor() is RequestResult.Success
}

private fun RequestResult<Unit>.errorCodeOrNull(): RequestErrorCode? =
    (this as? RequestResult.Error)?.code
