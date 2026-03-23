package com.rickroll.template.android.di

import com.rickroll.template.android.presentation.auth.AuthViewModel
import com.rickroll.template.android.presentation.player.PlayerViewModel
import com.rickroll.template.android.presentation.root.AppViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidPresentationModule = module {
    viewModel { AppViewModel(get()) }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { PlayerViewModel(get(), get()) }
}
