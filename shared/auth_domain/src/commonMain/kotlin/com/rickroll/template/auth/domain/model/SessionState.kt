package com.rickroll.template.auth.domain.model

sealed interface SessionState {
    data object Unauthorized : SessionState

    data class Authorized(
        val userId: Long,
        val login: String,
        val role: String,
    ) : SessionState
}
