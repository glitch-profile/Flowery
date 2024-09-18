package com.glitchcode.flowery.core.di

import com.glitchcode.flowery.login.domain.usecases.AuthUseCase
import org.koin.dsl.module

val useCasesModule = module {
    single {
        AuthUseCase(
            apiAuthRepository = get(),
            localAuthRepository = get(),
            localNotificationsSettings = get()
        )
    }
}