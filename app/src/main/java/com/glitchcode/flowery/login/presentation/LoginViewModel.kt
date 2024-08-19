package com.glitchcode.flowery.login.presentation

import androidx.lifecycle.ViewModel
import com.glitchcode.flowery.core.presentation.components.notification.SwipeableNotificationType
import com.glitchcode.flowery.core.presentation.components.notification.SwipeableNotificationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {

    private val notificationViewModel = SwipeableNotificationViewModel()
    val notificationState = notificationViewModel.notificationState

    fun showNotification(
        titleRes: Int,
        textRes: Int
    ) {
        notificationViewModel.showNotification(
            titleRes = titleRes,
            textRes = textRes,
            type = SwipeableNotificationType.NOT_SPECIFIED
        )
    }

}