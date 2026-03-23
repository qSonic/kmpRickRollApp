package com.rickroll.template.core.result

sealed interface RequestResult<out T> {
    data class Success<T>(val data: T) : RequestResult<T>

    data class Error(
        val code: RequestErrorCode = RequestErrorCode.UNKNOWN,
        val message: String? = null,
    ) : RequestResult<Nothing>
}

enum class RequestErrorCode {
    EMPTY_LOGIN,
    EMPTY_PASSWORD,
    LOGIN_ALREADY_EXISTS,
    INVALID_CREDENTIALS,
    DATABASE,
    UNKNOWN,
}
