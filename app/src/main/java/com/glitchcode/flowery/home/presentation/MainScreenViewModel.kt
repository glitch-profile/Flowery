package com.glitchcode.flowery.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glitchcode.flowery.core.domain.repository.LocalAuthDataRepository
import com.glitchcode.flowery.core.domain.utils.Resource
import com.glitchcode.flowery.login.domain.usecases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "MAIN_VIEW_MODEL"

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val localAuthDataRepository: LocalAuthDataRepository,
    private val authUseCase: AuthUseCase
): ViewModel() {

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        updateAuthInfo()
    }

    private fun updateAuthInfo() {
        viewModelScope.launch {
            val result = authUseCase.updateAuthInfo()
            if (result is Resource.Error) {
                Log.e(TAG, "updateAuthInfo-fail: ${result.message!!}")
                _isLoggedIn.update { false }
            }
        }
    }

    fun logout(
        onLogoutComplete: () -> Unit
    ) {
        viewModelScope.launch {
            authUseCase.logout()
            onLogoutComplete.invoke()
        }
    }

}