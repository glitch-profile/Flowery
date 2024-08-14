package com.glitchcode.flowery.core.presentation.mainactivity

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.glitchcode.flowery.core.domain.repository.LocalAppearanceSettingsRepository
import com.glitchcode.flowery.core.domain.repository.LocalAuthDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val IS_USING_SYSTEM_THEME = "isUsingSystemTheme"
private const val IS_USING_DARK_THEME = "isUsingDarkTheme"
private const val IS_USING_DYNAMIC_COLOR = "isUsingDynamicColor"

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val localAuthData: LocalAuthDataRepository,
    private val appearanceSettings: LocalAppearanceSettingsRepository
): ViewModel() {

    val startDestination = getStartDestination()

    private val _isUseSystemColorTheme = MutableStateFlow(true)
    val isUseSystemColorTheme = _isUseSystemColorTheme.asStateFlow()
    private val _isUseDarkTheme = MutableStateFlow(false)
    val isUseDarkTheme = _isUseDarkTheme.asStateFlow()
    private val _isUseDynamicColors = MutableStateFlow(false)
    val isUseDynamicColors = _isUseDynamicColors.asStateFlow()

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == IS_USING_SYSTEM_THEME) {
            _isUseSystemColorTheme.update { appearanceSettings.getIsUsingSystemTheme() }
        }
        if (key == IS_USING_DARK_THEME) {
            _isUseDarkTheme.update { appearanceSettings.getIsUsingDarkTheme() }
        }
        if (key == IS_USING_DYNAMIC_COLOR) {
            _isUseDynamicColors.update { appearanceSettings.getIsUsingDynamicColor() }
        }
    }

    fun registerAppearancePrefsListener() {
        appearanceSettings.preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterAppearancePrefsListener() {
        appearanceSettings.preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    init {
        loadAppearanceSettings()
    }

    private fun loadAppearanceSettings() {
        _isUseSystemColorTheme.update { appearanceSettings.getIsUsingSystemTheme() }
        _isUseDarkTheme.update { appearanceSettings.getIsUsingDarkTheme() }
        _isUseDynamicColors.update { appearanceSettings.getIsUsingDynamicColor() }
    }

    private fun getStartDestination(): String {
        return if (localAuthData.getUserSessionId() != null) {
            "main-screen"
        } else {
            "login-screen"
        }
    }

}