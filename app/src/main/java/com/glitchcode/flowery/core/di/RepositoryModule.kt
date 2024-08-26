package com.glitchcode.flowery.core.di

import com.glitchcode.flowery.core.data.repositoryimpl.LocalAppearanceSettingsRepositoryImpl
import com.glitchcode.flowery.core.data.repositoryimpl.LocalAuthDataRepositoryImpl
import com.glitchcode.flowery.core.data.repositoryimpl.LocalNotificationsSettingsRepositoryImpl
import com.glitchcode.flowery.core.domain.repository.LocalAppearanceSettingsRepository
import com.glitchcode.flowery.core.domain.repository.LocalAuthDataRepository
import com.glitchcode.flowery.core.domain.repository.LocalNotificationsSettingsRepository
import com.glitchcode.flowery.login.data.repository.RemoteAuthRepositoryImpl
import com.glitchcode.flowery.login.domain.repository.RemoteAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsLocalAuthDataRepository(
        localAuthDataRepositoryImpl: LocalAuthDataRepositoryImpl
    ): LocalAuthDataRepository

    @Binds
    @Singleton
    abstract fun bindsLocalAppearanceSettingsRepository(
        localAppearanceSettingsRepositoryImpl: LocalAppearanceSettingsRepositoryImpl
    ): LocalAppearanceSettingsRepository

    @Binds
    @Singleton
    abstract fun bindsNotificationsSettingsRepository(
        localNotificationsSettingsRepositoryImpl: LocalNotificationsSettingsRepositoryImpl
    ): LocalNotificationsSettingsRepository

    @Binds
    @Singleton
    abstract fun bindsRemoteAuthRepository(
        remoteAuthRepositoryImpl: RemoteAuthRepositoryImpl
    ): RemoteAuthRepository

}