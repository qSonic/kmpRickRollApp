package com.rickroll.template.shared.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.includes

typealias KoinAppDeclaration = KoinApplication.() -> Unit

fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin {
        includes(config)
        modules(
            sharedModule,
            platformModule,
        )
    }
}
