package com.glitchcode.flowery.core.presentation.components.notification

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SwipeableNotificationState() {

    private val _notificationState = MutableStateFlow(SwipeableNotificationData())
    val notificationState = _notificationState.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Default + Job())

    fun showNotification(
        titleRes: Int,
        textRes: Int,
        type: SwipeableNotificationType = SwipeableNotificationType.NOT_SPECIFIED
    ) {
        scope.coroutineContext.cancelChildren()
        scope.launch {
            _notificationState.update {
                SwipeableNotificationData(
                    titleRes = titleRes,
                    textRes = textRes,
                    type = type,
                    visibility = SwipeableNotificationVisibility.VISIBLE
                )
            }
            delay(5_000L)
            _notificationState.update {
                it.copy(visibility = SwipeableNotificationVisibility.HIDDEN)
            }
        }
    }

    fun dismissNotification() {
        _notificationState.update {
            it.copy(visibility = SwipeableNotificationVisibility.HIDDEN)
        }
        scope.coroutineContext.cancelChildren()
    }

}