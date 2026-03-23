package com.rickroll.template.shared.di

import com.rickroll.template.auth.data.di.authDataModule
import com.rickroll.template.auth.domain.di.authDomainModule
import com.rickroll.template.player.data.di.playerDataModule
import com.rickroll.template.player.domain.di.playerDomainModule
import org.koin.dsl.module

val sharedModule = module {
    includes(authDataModule, playerDataModule, authDomainModule, playerDomainModule)
}
