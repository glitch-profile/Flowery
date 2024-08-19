package com.glitchcode.flowery.core.presentation.components.notification

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SwipeableNotification(
    notificationState: SwipeableNotificationState
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomStart
    ) {
        val notificationData = notificationState.notificationState.collectAsState().value

        val scope = rememberCoroutineScope()
        val maxHeight = 200f
        val translationY = remember {
            Animatable(0f, maxHeight)
        }
        val decay = rememberSplineBasedDecay<Float>()
        translationY.updateBounds(0f, maxHeight)
        val draggableState = rememberDraggableState(
            onDelta = { dragAmount ->
                scope.launch {
                    translationY.snapTo(translationY.value + dragAmount)
                }
            }
        )

        LaunchedEffect(notificationData.visibility) {
            if (notificationData.visibility == SwipeableNotificationVisibility.VISIBLE) {
                if (translationY.value != 0f) translationY.animateTo(
                    0f,
                    animationSpec = spring(
                        stiffness = 500f
                    )
                )
            } else {
                if (translationY.value != maxHeight) translationY.animateTo(
                    maxHeight,
                    animationSpec = spring(
                        stiffness = 500f
                    )
                )
            }
        }

        if (translationY.value != maxHeight) {
            Surface(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .graphicsLayer {
                        this.translationY = translationY.value
                        this.alpha = (maxHeight - translationY.value) / maxHeight
                    }
                    .draggable(
                        draggableState,
                        enabled = notificationData.visibility == SwipeableNotificationVisibility.VISIBLE,
                        orientation = Orientation.Vertical,
                        onDragStopped = { velocity ->
                            if (notificationData.visibility == SwipeableNotificationVisibility.HIDDEN) return@draggable
                            val decayY = decay.calculateTargetValue(
                                translationY.value,
                                velocity
                            )
                            scope.launch {
                                val targetY = if (decayY > maxHeight * 0.5) maxHeight
                                else 0f

                                val canReachTargetWithDecay =
                                    (decayY > targetY && targetY == maxHeight)
                                            || (decayY < targetY && targetY == 0f)

                                if (canReachTargetWithDecay) {
                                    translationY.animateDecay(
                                        initialVelocity = velocity,
                                        animationSpec = decay
                                    )
                                } else {
                                    translationY.animateTo(
                                        targetValue = targetY,
                                        initialVelocity = velocity,
                                        animationSpec = spring(
                                            stiffness = 500f
                                        )
                                    )
                                }
                                if (targetY == maxHeight) notificationState.dismissNotification()
                            }
                        }
                    ),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    Icon(
//                        imageVector = Icons.Default.Delete,
//                        contentDescription = null,
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = notificationData.titleRes),
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = notificationData.textRes),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}