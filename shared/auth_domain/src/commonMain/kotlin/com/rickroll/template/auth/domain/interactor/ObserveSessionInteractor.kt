package com.rickroll.template.auth.domain.interactor

import com.rickroll.template.auth.domain.model.SessionState
import com.rickroll.template.auth.domain.repository.AuthRepository
import com.rickroll.template.core.result.RequestResult
import kotlinx.coroutines.flow.Flow

class ObserveSessionInteractor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<RequestResult<SessionState>> = authRepository.observeSession()
}
