package com.rickroll.template.auth.data.security

object PasswordHasher {
    fun hash(value: String): String {
        var hash = 5381L
        value.forEach { char ->
            hash = ((hash shl 5) + hash) + char.code
        }
        return hash.toString()
    }

    fun verify(rawPassword: String, hashedPassword: String): Boolean =
        hash(rawPassword) == hashedPassword
}
