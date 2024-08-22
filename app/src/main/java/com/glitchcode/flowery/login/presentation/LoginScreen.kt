package com.glitchcode.flowery.login.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.glitchcode.flowery.R
import com.glitchcode.flowery.core.presentation.components.FloweryTextButton
import com.glitchcode.flowery.core.presentation.components.FloweryTextButtonCompact
import com.glitchcode.flowery.core.presentation.components.FloweryTextField
import com.glitchcode.flowery.core.presentation.components.notification.SwipeableNotification

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val notificationState = viewModel.notificationState

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        FloweryTextButtonCompact(
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
        Spacer(modifier = Modifier.height(16.dp))
        var textValue by remember {
            mutableStateOf("")
        }
        FloweryTextField(
            value = textValue,
            onValueChanges = {
                textValue = it
            },
            titleText = "Description",
            labelText = "Enter description..."
        )
    }

    SwipeableNotification(notificationState = notificationState)
}