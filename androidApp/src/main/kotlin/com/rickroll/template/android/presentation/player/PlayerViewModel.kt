package com.rickroll.template.android.presentation.player

import androidx.lifecycle.viewModelScope
import com.rickroll.template.android.presentation.mvi.MviViewModel
import com.rickroll.template.auth.domain.interactor.LogoutInteractor
import com.rickroll.template.core.result.RequestResult
import com.rickroll.template.player.domain.interactor.GetRickRollMediaInteractor
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val getRickRollMediaInteractor: GetRickRollMediaInteractor,
    private val logoutInteractor: LogoutInteractor,
) : MviViewModel<PlayerState, PlayerEvent, PlayerEffect>(PlayerState()) {

    init {
        loadMedia()
    }

    override fun handleEvent(event: PlayerEvent) {
        when (event) {
            PlayerEvent.LogoutClicked -> logout()
        }
    }

    private fun loadMedia() {
        viewModelScope.launch {
            when (val result = getRickRollMediaInteractor()) {
                is RequestResult.Success -> {
                    val media = result.data
                    setState {
                        copy(
                            title = media.title,
                            mediaUrl = media.url,
                            isLoading = false,
                        )
                    }
                }

                is RequestResult.Error -> {
                    setState {
                        copy(
                            isLoading = false,
                            errorMessage = "Не удалось загрузить видео",
                        )
                    }
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutInteractor()
            sendEffect(PlayerEffect.LoggedOut)
        }
    }
}
