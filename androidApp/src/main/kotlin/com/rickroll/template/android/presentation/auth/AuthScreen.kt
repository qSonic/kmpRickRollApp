package com.rickroll.template.android.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rickroll.template.auth.domain.model.AuthMode
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthRoute(
    viewModel: AuthViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AuthScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun AuthScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "RickRoll Auth",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = { onEvent(AuthEvent.ModeChanged(AuthMode.LOGIN)) },
            ) {
                Text(text = "Login")
            }
            OutlinedButton(
                onClick = { onEvent(AuthEvent.ModeChanged(AuthMode.REGISTER)) },
            ) {
                Text(text = "Register")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.login,
            onValueChange = { onEvent(AuthEvent.LoginChanged(it)) },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
        )

        state.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onEvent(AuthEvent.Submit) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = if (state.mode == AuthMode.LOGIN) "Войти" else "Зарегистрироваться",
            )
        }
    }
}
