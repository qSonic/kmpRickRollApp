package com.rickroll.template.auth.data.di

import com.rickroll.template.auth.data.db.AppDatabaseProvider
import com.rickroll.template.auth.data.repository.AuthRepositoryImpl
import com.rickroll.template.auth.domain.repository.AuthRepository
import org.koin.dsl.module

val authDataModule = module {
    single { AppDatabaseProvider() }
    single<AuthRepository> { AuthRepositoryImpl(get<AppDatabaseProvider>().database) }
}
