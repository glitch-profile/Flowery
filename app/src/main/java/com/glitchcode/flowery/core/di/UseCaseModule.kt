package com.glitchcode.flowery.core.di

import com.glitchcode.flowery.core.domain.repository.LocalAuthDataRepository
import com.glitchcode.flowery.core.domain.repository.LocalNotificationsSettingsRepository
import com.glitchcode.flowery.login.domain.repository.RemoteAuthRepository
import com.glitchcode.flowery.login.domain.usecases.AuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideAuthUseCase(
        apiAuthRepository: RemoteAuthRepository,
        localAuthRepository: LocalAuthDataRepository,
        notificationsSettingsRepository: LocalNotificationsSettingsRepository
    ): AuthUseCase {
        return AuthUseCase(
            apiAuthRepository = apiAuthRepository,
            localAuthRepository = localAuthRepository,
            localNotificationsSettings = notificationsSettingsRepository
        )
    }
}