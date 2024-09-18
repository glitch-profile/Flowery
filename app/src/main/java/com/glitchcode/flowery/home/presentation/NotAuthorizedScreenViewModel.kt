package com.glitchcode.flowery.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glitchcode.flowery.R
import com.glitchcode.flowery.core.domain.utils.Resource
import com.glitchcode.flowery.core.presentation.components.notification.SwipeableNotificationState
import com.glitchcode.flowery.login.domain.usecases.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotAuthorizedScreenViewModel(
    private val authUseCase: AuthUseCase
): ViewModel() {

    val notificationState = SwipeableNotificationState()

    private val _isLoggingOut = MutableStateFlow(false)
    val isLoggingOut = _isLoggingOut.asStateFlow()

    fun logout(onLoggedOut: () -> Unit) {
        if (isLoggingOut.value) return
        viewModelScope.launch {
            _isLoggingOut.update { true }
            val result = authUseCase.logout()
            if (result is Resource.Success) {
                onLoggedOut.invoke()
            } else {
                notificationState.showNotification(
                    titleRes = R.string.not_authorized_screen_notification_title,
                    textRes = R.string.not_authorized_screen_notification_text
                )
            }
            _isLoggingOut.update { false }
        }
    }

}