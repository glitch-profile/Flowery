package com.glitchcode.flowery.core.domain.repository

import android.content.SharedPreferences

interface LocalNotificationsSettingsRepository {

    val preferences: SharedPreferences

    fun getSavedFCMToken(): String?
    fun setSavedFcmToken(value: String?)

}