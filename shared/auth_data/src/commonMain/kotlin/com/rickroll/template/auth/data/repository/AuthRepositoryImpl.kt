package com.rickroll.template.auth.data.repository

import com.rickroll.template.auth.data.db.AppDatabase
import com.rickroll.template.auth.data.security.PasswordHasher
import com.rickroll.template.auth.domain.model.SessionState
import com.rickroll.template.auth.domain.repository.AuthRepository
import com.rickroll.template.core.result.RequestErrorCode
import com.rickroll.template.core.result.RequestResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Clock

class AuthRepositoryImpl(
    private val database: AppDatabase,
) : AuthRepository {

    private val sessionState = MutableStateFlow(readSession())

    override fun login(login: String, password: String): RequestResult<Unit> {
        val normalizedLogin = login.trim()
        if (normalizedLogin.isEmpty()) return RequestResult.Error(RequestErrorCode.EMPTY_LOGIN)
        if (password.isEmpty()) return RequestResult.Error(RequestErrorCode.EMPTY_PASSWORD)

        return runCatching {
            val user = database.appDatabaseQueries.selectUserByLogin(normalizedLogin).executeAsOneOrNull()
                ?: return RequestResult.Error(RequestErrorCode.INVALID_CREDENTIALS)

            if (!PasswordHasher.verify(password, user.password_hash)) {
                return RequestResult.Error(RequestErrorCode.INVALID_CREDENTIALS)
            }

            database.appDatabaseQueries.upsertSession(
                user_id = user.id,
                created_at = kotlin.time.Clock.System.now().toEpochMilliseconds(),
            )
            sessionState.value = RequestResult.Success(SessionState.Authorized(user.id, user.login, user.role))
            RequestResult.Success(Unit)
        }.getOrElse { error ->
            RequestResult.Error(RequestErrorCode.DATABASE, error.message)
        }
    }

    override fun register(login: String, password: String): RequestResult<Unit> {
        val normalizedLogin = login.trim()
        if (normalizedLogin.isEmpty()) return RequestResult.Error(RequestErrorCode.EMPTY_LOGIN)
        if (password.isEmpty()) return RequestResult.Error(RequestErrorCode.EMPTY_PASSWORD)

        return runCatching {
            val existing = database.appDatabaseQueries.selectUserByLogin(normalizedLogin).executeAsOneOrNull()
            if (existing != null) return RequestResult.Error(RequestErrorCode.LOGIN_ALREADY_EXISTS)

            database.appDatabaseQueries.insertUser(
                login = normalizedLogin,
                password_hash = PasswordHasher.hash(password),
                role = "USER",
                created_at = Clock.System.now().toEpochMilliseconds(),
            )

            val newUser = database.appDatabaseQueries.selectUserByLogin(normalizedLogin).executeAsOneOrNull()
                ?: return RequestResult.Error(RequestErrorCode.INVALID_CREDENTIALS)

            database.appDatabaseQueries.upsertSession(
                user_id = newUser.id,
                created_at = Clock.System.now().toEpochMilliseconds(),
            )
            sessionState.value = RequestResult.Success(SessionState.Authorized(newUser.id, newUser.login, newUser.role))
            RequestResult.Success(Unit)
        }.getOrElse { error ->
            RequestResult.Error(RequestErrorCode.DATABASE, error.message)
        }
    }

    override fun logout(): RequestResult<Unit> = runCatching {
        database.appDatabaseQueries.clearSession()
        sessionState.value = RequestResult.Success(SessionState.Unauthorized)
        RequestResult.Success(Unit)
    }.getOrElse { error ->
        RequestResult.Error(RequestErrorCode.DATABASE, error.message)
    }

    override fun getSession(): RequestResult<SessionState> {
        val session = readSession()
        sessionState.value = session
        return session
    }

    override fun observeSession(): Flow<RequestResult<SessionState>> = sessionState.asStateFlow()

    private fun readSession(): RequestResult<SessionState> = runCatching {
        val session = database.appDatabaseQueries.selectSession().executeAsOneOrNull()
        if (session == null) {
            RequestResult.Success(SessionState.Unauthorized)
        } else {
            RequestResult.Success(
                SessionState.Authorized(
                    userId = session.user_id,
                    login = session.login,
                    role = session.role,
                ),
            )
        }
    }.getOrElse { error ->
        RequestResult.Error(RequestErrorCode.DATABASE, error.message)
    }
}
