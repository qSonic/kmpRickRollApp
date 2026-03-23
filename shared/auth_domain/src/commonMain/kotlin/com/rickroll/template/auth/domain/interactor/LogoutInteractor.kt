package com.rickroll.template.auth.domain.interactor

import com.rickroll.template.auth.domain.repository.AuthRepository
import com.rickroll.template.core.result.RequestResult

class LogoutInteractor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): RequestResult<Unit> = authRepository.logout()
}
