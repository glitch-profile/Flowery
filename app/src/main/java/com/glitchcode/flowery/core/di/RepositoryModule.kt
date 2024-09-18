package com.glitchcode.flowery.core.di

import com.glitchcode.flowery.core.data.repositoryimpl.LocalAppearanceSettingsRepositoryImpl
import com.glitchcode.flowery.core.data.repositoryimpl.LocalAuthDataRepositoryImpl
import com.glitchcode.flowery.core.data.repositoryimpl.LocalNotificationsSettingsRepositoryImpl
import com.glitchcode.flowery.core.domain.repository.LocalAppearanceSettingsRepository
import com.glitchcode.flowery.core.domain.repository.LocalAuthDataRepository
import com.glitchcode.flowery.core.domain.repository.LocalNotificationsSettingsRepository
import com.glitchcode.flowery.login.data.repository.RemoteAuthRepositoryImpl
import com.glitchcode.flowery.login.domain.repository.RemoteAuthRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    // local repositories
    single<LocalAuthDataRepository> {
        LocalAuthDataRepositoryImpl(get())
    }

    single<LocalNotificationsSettingsRepository> {
        LocalNotificationsSettingsRepositoryImpl(get())
    }

    single<LocalAppearanceSettingsRepository> {
        LocalAppearanceSettingsRepositoryImpl(get())
    }

    // remote sources
    single<RemoteAuthRepository> {
        RemoteAuthRepositoryImpl(get(qualifier = named("HttpServer")))
    }
}