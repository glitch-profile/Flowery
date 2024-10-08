package com.glitchcode.flowery.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.glitchcode.flowery.R
import com.glitchcode.flowery.core.presentation.components.FloweryButton
import com.glitchcode.flowery.core.presentation.components.notification.SwipeableNotification
import com.glitchcode.flowery.core.theme.icons.FloweryIcons
import com.glitchcode.flowery.core.theme.icons.Warning
import org.koin.androidx.compose.koinViewModel

// TODO: When main content on home screen will be ready, rework this screen to a component, that will open on top of content

@Composable
fun NotAuthorizedScreen(
    onLoginClicked: () -> Unit,
    viewModel: NotAuthorizedScreenViewModel = koinViewModel()
) {
    
    val notificationState = viewModel.notificationState
    val isLoggingOut = viewModel.isLoggingOut.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Column {
                Spacer(modifier = Modifier.height(37.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .clip(shape = MaterialTheme.shapes.medium)
                        .background(color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp, top = 45.dp), // top = 8 (not 16) + half height of icon
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.not_authorized_screen_title),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.not_authorized_screen_text),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    FloweryButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = { viewModel.logout(onLoginClicked) },
                    ) {
                        Text(text = stringResource(id = R.string.not_authorized_screen_login_button_text))
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
                                    strokeWidth = 3.dp,
                                    color = LocalContentColor.current
                                )
                            }
                        }
                    }
                }
            }
            Icon(
                modifier = Modifier
                    .size(74.dp)
                    .align(Alignment.TopCenter),
                imageVector = FloweryIcons.Warning,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        }
    }

    SwipeableNotification(notificationState = notificationState)
}