package com.rickroll.template.auth.data.db

class AppDatabase {
    val appDatabaseQueries = AppDatabaseQueries()
}

class AppDatabaseQueries {
    private val users = mutableListOf<UserRow>()
    private var nextUserId = 1L
    private var sessionUserId: Long? = null

    fun selectUserByLogin(login: String): QueryResult<UserRow> =
        QueryResult(users.firstOrNull { it.login == login })

    fun insertUser(login: String, password_hash: String, role: String, created_at: Long) {
        users.add(
            UserRow(
                id = nextUserId++,
                login = login,
                password_hash = password_hash,
                role = role,
                created_at = created_at,
            ),
        )
    }

    fun insertDefaultAdmin(login: String, password_hash: String, role: String, created_at: Long) {
        if (users.none { it.login == login }) {
            insertUser(login, password_hash, role, created_at)
        }
    }

    fun upsertSession(user_id: Long, created_at: Long) {
        sessionUserId = user_id
    }

    fun selectSession(): QueryResult<SessionRow> {
        val userId = sessionUserId ?: return QueryResult(null)
        val user = users.firstOrNull { it.id == userId } ?: return QueryResult(null)
        return QueryResult(
            SessionRow(
                user_id = user.id,
                login = user.login,
                role = user.role,
            ),
        )
    }

    fun clearSession() {
        sessionUserId = null
    }
}

data class UserRow(
    val id: Long,
    val login: String,
    val password_hash: String,
    val role: String,
    val created_at: Long,
)

data class SessionRow(
    val user_id: Long,
    val login: String,
    val role: String,
)

class QueryResult<T>(private val value: T?) {
    fun executeAsOneOrNull(): T? = value
}
