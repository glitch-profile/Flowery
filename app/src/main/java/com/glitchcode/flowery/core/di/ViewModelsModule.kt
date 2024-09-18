package com.glitchcode.flowery.core.di

import com.glitchcode.flowery.core.presentation.mainactivity.MainActivityViewModel
import com.glitchcode.flowery.home.presentation.MainScreenViewModel
import com.glitchcode.flowery.home.presentation.NotAuthorizedScreenViewModel
import com.glitchcode.flowery.login.presentation.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel {
        MainActivityViewModel(
            localAuthData = get(),
            appearanceSettings = get()
        )
    }

    viewModel {
        MainScreenViewModel(
            localAuthDataRepository = get(),
            authUseCase = get()
        )
    }

    viewModel {
        LoginViewModel(
            localAuthDataRepository = get(),
            authUseCase = get()
        )
    }

    viewModel {
        NotAuthorizedScreenViewModel(
            authUseCase = get()
        )
    }
}