package com.glitchcode.flowery.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glitchcode.flowery.core.domain.repository.LocalAuthDataRepository
import com.glitchcode.flowery.core.domain.utils.Resource
import com.glitchcode.flowery.login.domain.usecases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "MAIN_VIEW_MODEL"

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val localAuthDataRepository: LocalAuthDataRepository,
    private val authUseCase: AuthUseCase
): ViewModel() {

    // TODO: when NotAuthorizedScreen will be a component, replace this with a bool state
    private val _isLoggedIn = Channel<Boolean>()
    val isLoggedIn = _isLoggedIn.receiveAsFlow()

    init {
        updateAuthInfo()
    }

    private fun updateAuthInfo() {
        viewModelScope.launch {
            val result = authUseCase.updateAuthInfo()
            if (result is Resource.Error) {
                _isLoggedIn.send(false)
            }
        }
    }

    fun logout(
        onLogoutComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val result = authUseCase.logout()
            if (result is Resource.Success) onLogoutComplete.invoke()
        }
    }

}