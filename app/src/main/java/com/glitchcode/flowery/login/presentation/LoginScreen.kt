package com.glitchcode.flowery.login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.glitchcode.flowery.R
import com.glitchcode.flowery.core.presentation.components.FloweryButton
import com.glitchcode.flowery.core.presentation.components.FloweryTextField
import com.glitchcode.flowery.core.presentation.components.notification.SwipeableNotification

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val notificationState = viewModel.notificationState
    var number by remember {
        mutableIntStateOf(0)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FloweryButton(
            modifier = Modifier,
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
            labelText = "Enter description...",
            charactersLimit = 100
        )
//        Spacer(modifier = Modifier.height(16.dp))
//        FloweryTextField(
//            value = textValue,
//            onValueChanges = {
//                textValue = it
//            },
//            titleText = "Description",
//            labelText = "Enter description...",
//            charactersLimit = 100
//        )
    }

    SwipeableNotification(notificationState = notificationState)
}