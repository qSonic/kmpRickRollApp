package com.rickroll.template.shared.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.module.Module

fun initKoinAndroid(
    context: Context,
    appModules: List<Module> = emptyList(),
) {
    initKoin {
        androidContext(context)
        androidLogger()
        if (appModules.isNotEmpty()) {
            modules(appModules)
        }
    }
}
