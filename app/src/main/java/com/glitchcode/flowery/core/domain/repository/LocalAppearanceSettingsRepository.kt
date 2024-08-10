package com.glitchcode.flowery.core.domain.repository

import android.content.SharedPreferences

interface LocalAppearanceSettingsRepository {

    val preferences: SharedPreferences

    fun getIsUsingSystemTheme(): Boolean
    fun setIsUsingSystemTheme(value: Boolean)

    fun getIsUsingDarkTheme(): Boolean
    fun setIsUsingDarkTheme(value: Boolean)

    fun getIsUsingDynamicColor(): Boolean
    fun setIsUsingDynamicColor(value: Boolean)

    fun getIsUsingSystemLocale(): Boolean
    fun setIsUsingSystemLocale(value: Boolean)

}