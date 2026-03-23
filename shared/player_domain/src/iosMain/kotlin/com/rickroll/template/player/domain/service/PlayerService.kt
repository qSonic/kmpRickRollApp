package com.rickroll.template.player.domain.service

import com.rickroll.template.core.result.RequestResult
import com.rickroll.template.player.domain.interactor.GetRickRollMediaInteractor
import com.rickroll.template.player.domain.model.MediaItem
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlayerService : KoinComponent {
    private val getRickRollMediaInteractor: GetRickRollMediaInteractor by inject()

    fun getRickRollMediaOrNull(): MediaItem? {
        val result = getRickRollMediaInteractor()
        return (result as? RequestResult.Success)?.data
    }
}
