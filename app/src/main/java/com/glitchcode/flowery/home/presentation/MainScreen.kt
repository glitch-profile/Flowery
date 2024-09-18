package com.glitchcode.flowery.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.glitchcode.flowery.core.presentation.components.FloweryButton
import com.glitchcode.flowery.core.presentation.components.notification.SwipeableNotification
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onNavigateToLoginScreen: () -> Unit,
    onLoginDataOutdated: () -> Unit,
    viewModel: MainScreenViewModel = koinViewModel()
) {
    val notificationState = viewModel.notificationState
    
    LaunchedEffect(null) {
        viewModel.isLoggedIn.collectLatest { isLoggedIn ->
            if (!isLoggedIn) onLoginDataOutdated.invoke()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val isLoggingOut = viewModel.isLoggingOut.collectAsState()

        FloweryButton(
            onClick = {
                viewModel.logout(onNavigateToLoginScreen)
            }
        ) {
            Text(text = "Logout")
            AnimatedVisibility(
                visible = isLoggingOut.value,
                enter = expandHorizontally(
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                ),
                exit = shrinkHorizontally(
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                )
            ) {
                Row {
                    Spacer(modifier = Modifier.width(12.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 3.dp
                    )
                }
            }
        }
    }
    
    SwipeableNotification(notificationState = notificationState)
}