package com.rickroll.template.player.data.di

import com.rickroll.template.player.data.repository.MediaRepositoryImpl
import com.rickroll.template.player.domain.repository.MediaRepository
import org.koin.dsl.module

val playerDataModule = module {
    single<MediaRepository> { MediaRepositoryImpl() }
}
