package com.rickroll.template.auth.domain.interactor

import com.rickroll.template.auth.domain.model.SessionState
import com.rickroll.template.auth.domain.repository.AuthRepository
import com.rickroll.template.core.result.RequestResult

class GetSessionInteractor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): RequestResult<SessionState> = authRepository.getSession()
}
