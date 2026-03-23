package com.rickroll.template.android.presentation.root

sealed class AppRoute(val route: String) {
    data object Auth : AppRoute("auth")
    data object Player : AppRoute("player")
}
