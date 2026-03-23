package com.rickroll.template.android.presentation.player

data class PlayerState(
    val title: String = "",
    val mediaUrl: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)

sealed interface PlayerEvent {
    data object LogoutClicked : PlayerEvent
}

sealed interface PlayerEffect {
    data object LoggedOut : PlayerEffect
}
