package com.rickroll.template.player.domain.repository

import com.rickroll.template.core.result.RequestResult
import com.rickroll.template.player.domain.model.MediaItem

interface MediaRepository {
    fun getRickRollMedia(): RequestResult<MediaItem>
}
