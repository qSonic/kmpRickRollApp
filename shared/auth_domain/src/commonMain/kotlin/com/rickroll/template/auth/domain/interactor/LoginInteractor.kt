package com.rickroll.template.auth.domain.interactor

import com.rickroll.template.auth.domain.repository.AuthRepository
import com.rickroll.template.core.result.RequestResult

class LoginInteractor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(login: String, password: String): RequestResult<Unit> =
        authRepository.login(login, password)
}
