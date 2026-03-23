package com.rickroll.template.auth.data.db

import com.rickroll.template.auth.data.security.PasswordHasher
import kotlin.time.Clock

class AppDatabaseProvider {
    val database: AppDatabase = AppDatabase().also { db ->
        db.appDatabaseQueries.insertDefaultAdmin(
            login = "admin",
            password_hash = PasswordHasher.hash("admin"),
            role = "ADMIN",
            created_at = Clock.System.now().toEpochMilliseconds(),
        )
    }
}
