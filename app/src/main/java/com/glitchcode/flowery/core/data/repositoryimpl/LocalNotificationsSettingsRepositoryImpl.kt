package com.glitchcode.flowery.core.data.repositoryimpl

import android.content.Context
import android.content.SharedPreferences
import com.glitchcode.flowery.core.domain.repository.LocalNotificationsSettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val PREFERENCES_NAME = "NotificationSettings"
private const val SAVED_FCM_TOKEN = "lastSavedFcmToken"

class LocalNotificationsSettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): LocalNotificationsSettingsRepository {

    override val preferences: SharedPreferences
        get() = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private var fcmToken: String? = null
    override fun getSavedFCMToken(): String? {
        return fcmToken ?: kotlin.run {
            fcmToken = preferences.getString(SAVED_FCM_TOKEN, null)
            fcmToken
        }
    }

    override fun setSavedFcmToken(value: String?) {
        fcmToken = value
        preferences.edit().putString(SAVED_FCM_TOKEN, value).apply()
    }
}