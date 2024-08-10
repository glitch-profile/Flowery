package com.glitchcode.flowery.core.presentation.mainactivity

import androidx.lifecycle.ViewModel
import com.glitchcode.flowery.core.domain.repository.LocalAppearanceSettingsRepository
import com.glitchcode.flowery.core.domain.repository.LocalAuthDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val localAuthData: LocalAuthDataRepository,
    private val appearanceSettings: LocalAppearanceSettingsRepository
): ViewModel() {



}