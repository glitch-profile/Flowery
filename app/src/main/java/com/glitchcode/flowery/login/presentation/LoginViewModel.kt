package com.glitchcode.flowery.login.presentation

import androidx.lifecycle.ViewModel
import com.glitchcode.flowery.core.presentation.components.notification.SwipeableNotificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {

    val notificationState = SwipeableNotificationState()

    fun showNotification(
        titleRes: Int,
        textRes: Int
    ) {
        notificationState.showNotification(
            titleRes = titleRes,
            textRes = textRes
        )
    }

}