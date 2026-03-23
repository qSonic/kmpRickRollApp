package com.rickroll.template.android.presentation.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickroll.template.auth.domain.interactor.ObserveSessionInteractor
import com.rickroll.template.auth.domain.model.SessionState
import com.rickroll.template.core.result.RequestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppState(
    val isAuthorized: Boolean = false,
)

class AppViewModel(
    private val observeSessionInteractor: ObserveSessionInteractor,
) : ViewModel() {

    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeSessionInteractor().collect { session ->
                _state.update {
                    val authorized = session is RequestResult.Success && session.data is SessionState.Authorized
                    it.copy(isAuthorized = authorized)
                }
            }
        }
    }
}
