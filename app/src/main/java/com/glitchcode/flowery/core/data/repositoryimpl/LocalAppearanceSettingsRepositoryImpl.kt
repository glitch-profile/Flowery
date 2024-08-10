package com.glitchcode.flowery.core.data.repositoryimpl

import android.content.Context
import android.content.SharedPreferences
import com.glitchcode.flowery.core.domain.repository.LocalAppearanceSettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext

private const val PREFERENCES_NAME = "AppearanceSettings"
private const val IS_USING_SYSTEM_THEME = "isUsingSystemTheme"
private const val IS_USING_DARK_THEME = "isUsingDarkTheme"
private const val IS_USING_DYNAMIC_COLOR = "isUsingDynamicColor"
private const val IS_USING_SYSTEM_LOCALE = "isUsingSystemLocale"

class LocalAppearanceSettingsRepositoryImpl(
    @ApplicationContext private val context: Context
): LocalAppearanceSettingsRepository {

    override val preferences: SharedPreferences
        get() = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private var isUsingSystemTheme: Boolean? = null
    override fun getIsUsingSystemTheme(): Boolean {
        return isUsingSystemTheme ?: kotlin.run {
            isUsingSystemTheme = preferences.getBoolean(IS_USING_SYSTEM_THEME, true)
            isUsingSystemTheme!!
        }
    }
    override fun setIsUsingSystemTheme(value: Boolean) {
        isUsingSystemTheme = value
        preferences.edit().putBoolean(IS_USING_SYSTEM_THEME, value).apply()
    }

    private var isUsingDarkTheme: Boolean? = null
    override fun getIsUsingDarkTheme(): Boolean {
        return isUsingDarkTheme ?: kotlin.run {
            isUsingDarkTheme = preferences.getBoolean(IS_USING_DARK_THEME, false)
            isUsingDarkTheme!!
        }
    }
    override fun setIsUsingDarkTheme(value: Boolean) {
        isUsingDarkTheme = value
        preferences.edit().putBoolean(IS_USING_DARK_THEME, value).apply()
    }

    private var isUsingDynamicColor: Boolean? = null
    override fun getIsUsingDynamicColor(): Boolean {
        return isUsingDynamicColor ?: kotlin.run {
            isUsingDynamicColor = preferences.getBoolean(IS_USING_DYNAMIC_COLOR, true)
            isUsingDynamicColor!!
        }
    }
    override fun setIsUsingDynamicColor(value: Boolean) {
        isUsingDynamicColor = value
        preferences.edit().putBoolean(IS_USING_DYNAMIC_COLOR, value).apply()
    }

    private var isUsingSystemLocale: Boolean? = null
    override fun getIsUsingSystemLocale(): Boolean {
        return isUsingSystemLocale ?: kotlin.run {
            isUsingSystemLocale = preferences.getBoolean(IS_USING_SYSTEM_LOCALE, true)
            isUsingSystemLocale!!
        }
    }
    override fun setIsUsingSystemLocale(value: Boolean) {
        isUsingSystemLocale = value
        preferences.edit().putBoolean(IS_USING_SYSTEM_LOCALE, value).apply()
    }
}