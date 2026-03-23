package com.rickroll.template.android

import android.app.Application
import com.rickroll.template.android.di.androidPresentationModule
import com.rickroll.template.shared.di.initKoinAndroid

class RickRollApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinAndroid(
            context = this,
            appModules = listOf(androidPresentationModule),
        )
    }
}
