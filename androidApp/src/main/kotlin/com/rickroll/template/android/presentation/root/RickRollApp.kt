package com.rickroll.template.android.presentation.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rickroll.template.android.presentation.auth.AuthRoute
import com.rickroll.template.android.presentation.player.PlayerRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun RickRollApp(
    appViewModel: AppViewModel = koinViewModel(),
) {
    val state by appViewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    LaunchedEffect(state.isAuthorized) {
        val targetRoute = if (state.isAuthorized) AppRoute.Player.route else AppRoute.Auth.route
        if (navController.currentDestination?.route != targetRoute) {
            navController.navigate(targetRoute) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppRoute.Auth.route,
    ) {
        composable(AppRoute.Auth.route) {
            AuthRoute()
        }
        composable(AppRoute.Player.route) {
            PlayerRoute()
        }
    }
}
