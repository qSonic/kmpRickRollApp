package com.rickroll.template.player.data.repository

import com.rickroll.template.core.result.RequestErrorCode
import com.rickroll.template.core.result.RequestResult
import com.rickroll.template.player.domain.model.MediaItem
import com.rickroll.template.player.domain.repository.MediaRepository

class MediaRepositoryImpl : MediaRepository {
    override fun getRickRollMedia(): RequestResult<MediaItem> = runCatching {
        MediaItem(
            url = "https://github.com/ShatteredDisk/rickroll/raw/refs/heads/main/rickroll.mp4",
            title = "Never Gonna Give You Up",
        )
    }.fold(
        onSuccess = { RequestResult.Success(it) },
        onFailure = { RequestResult.Error(RequestErrorCode.UNKNOWN, it.message) },
    )
}
