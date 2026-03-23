package com.rickroll.template.player.domain.interactor

import com.rickroll.template.core.result.RequestResult
import com.rickroll.template.player.domain.model.MediaItem
import com.rickroll.template.player.domain.repository.MediaRepository

class GetRickRollMediaInteractor(
    private val mediaRepository: MediaRepository,
) {
    operator fun invoke(): RequestResult<MediaItem> = mediaRepository.getRickRollMedia()
}
