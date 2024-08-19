package com.glitchcode.flowery.login.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.glitchcode.flowery.R
import com.glitchcode.flowery.core.presentation.components.notification.SwipeableNotificationHolder

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val notificationState = viewModel.notificationState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.Center),
            onClick = { 
                viewModel.showNotification(
                    R.string.swipeable_notification_unknown_error_title,
                    R.string.swipeable_notification_unknown_error_text
                )
            }
        ) {
            Text(text = "show notification")
        }
    }

    SwipeableNotificationHolder(notificationState = notificationState.value)
}