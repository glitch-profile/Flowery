package com.glitchcode.flowery.core.presentation.components.notification

import com.glitchcode.flowery.R

data class SwipeableNotificationState(
    val titleRes: Int = R.string.swipeable_notification_unknown_error_title,
    val textRes: Int = R.string.swipeable_notification_unknown_error_text,
    val visibility: SwipeableNotificationVisibility = SwipeableNotificationVisibility.HIDDEN,
    val type: SwipeableNotificationType = SwipeableNotificationType.NOT_SPECIFIED
)

enum class SwipeableNotificationType {
    ERROR, INFO, SUCCESS, NOT_SPECIFIED
}

enum class SwipeableNotificationVisibility {
    VISIBLE, HIDDEN
}
