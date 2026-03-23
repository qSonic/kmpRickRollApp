package com.rickroll.template.auth.domain.di

import com.rickroll.template.auth.domain.interactor.GetSessionInteractor
import com.rickroll.template.auth.domain.interactor.LoginInteractor
import com.rickroll.template.auth.domain.interactor.LogoutInteractor
import com.rickroll.template.auth.domain.interactor.ObserveSessionInteractor
import com.rickroll.template.auth.domain.interactor.RegisterInteractor
import org.koin.dsl.module

val authDomainModule = module {
    factory { GetSessionInteractor(get()) }
    factory { ObserveSessionInteractor(get()) }
    factory { LoginInteractor(get()) }
    factory { RegisterInteractor(get()) }
    factory { LogoutInteractor(get()) }
}
