package com.glitchcode.flowery.core.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FloweryFilledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    content: @Composable (RowScope.() -> Unit)

) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        enabled = isEnabled,
        colors = colors,
        content = content
    )
}

@Composable
fun FloweryTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean,
    colors: ButtonColors,
    content: @Composable RowScope.() -> Unit

) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        enabled = isEnabled,
        colors = colors,
        content = content
    )
}