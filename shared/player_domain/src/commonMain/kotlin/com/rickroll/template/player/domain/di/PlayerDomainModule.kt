package com.rickroll.template.player.domain.di

import com.rickroll.template.player.domain.interactor.GetRickRollMediaInteractor
import org.koin.dsl.module

val playerDomainModule = module {
    factory { GetRickRollMediaInteractor(get()) }
}
