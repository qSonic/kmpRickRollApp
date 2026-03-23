package com.rickroll.template.auth.domain.repository

import com.rickroll.template.auth.domain.model.SessionState
import com.rickroll.template.core.result.RequestResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(login: String, password: String): RequestResult<Unit>
    fun register(login: String, password: String): RequestResult<Unit>
    fun logout(): RequestResult<Unit>
    fun getSession(): RequestResult<SessionState>
    fun observeSession(): Flow<RequestResult<SessionState>>
}
