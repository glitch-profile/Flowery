package com.glitchcode.flowery.core.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun FloweryButton(
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
fun FloweryButtonWithIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        enabled = isEnabled,
        colors = colors,
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        content = content
    )
}

@Composable
fun FloweryTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
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

@Composable
fun FloweryTextButtonWithIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        enabled = isEnabled,
        colors = colors,
        contentPadding = ButtonDefaults.TextButtonWithIconContentPadding,
        content = content
    )
}

@Composable
fun FloweryTextButtonCompact(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        modifier = modifier
            .height(24.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        enabled = isEnabled,
        colors = colors,
        contentPadding = PaddingValues(horizontal = 4.dp),
        content = content
    )
}