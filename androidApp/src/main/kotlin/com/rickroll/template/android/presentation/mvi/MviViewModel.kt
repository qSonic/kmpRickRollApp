package com.rickroll.template.android.presentation.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class MviViewModel<State : Any, Event : Any, Effect : Any>(
    initialState: State,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val effect: SharedFlow<Effect> = _effect.asSharedFlow()

    fun onEvent(event: Event) {
        handleEvent(event)
    }

    protected abstract fun handleEvent(event: Event)

    protected fun setState(reducer: State.() -> State) {
        _state.update { current -> current.reducer() }
    }

    protected fun withState(block: (State) -> Unit) {
        block(_state.value)
    }

    protected fun sendEffect(effect: Effect) {
        _effect.tryEmit(effect)
    }
}
