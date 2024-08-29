package com.glitchcode.flowery.core.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun FlowerySolidTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    charactersLimit: Int? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
) {
    val placeHolderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)

    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = { newValue ->
            if (charactersLimit != null) {
                onValueChange.invoke(newValue.take(charactersLimit))
            } else onValueChange.invoke(newValue)
        },
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        textStyle = MaterialTheme.typography.bodyLarge,
        enabled = enabled,
        readOnly = readOnly,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            Column(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(contentPadding),
            ) {
                if (label != null) {
                    CompositionLocalProvider(
                        LocalTextStyle provides MaterialTheme.typography.titleMedium,
                        LocalContentColor provides MaterialTheme.colorScheme.primary
                    ) {
                        label.invoke()
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = modifier.weight(1f)
                    ) {
                        if (placeholder!= null && value.isEmpty()) {
                            CompositionLocalProvider(
                                LocalTextStyle provides MaterialTheme.typography.bodyLarge,
                                LocalContentColor provides placeHolderColor
                            ) {
                                placeholder.invoke()
                            }
                        }
                        innerTextField.invoke()
                    }
                    if (charactersLimit != null) {
                        val remainingCharactersCountColor = animateColorAsState(
                            targetValue = if (charactersLimit <= value.length) MaterialTheme.colorScheme.error
                            else placeHolderColor,
                            label = "remainingCharactersCountColorChange"
                        )
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                        ) {
                            CounterFixed(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd),
                                count = charactersLimit - value.length,
                                maxChars = charactersLimit.toString().length,
                                invertDirection = true,
                                style = MaterialTheme.typography.bodyLarge,
                                color = remainingCharactersCountColor.value
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun FloweryIndicatorTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    placeholderText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    val indicatorColor = animateColorAsState(
        targetValue = if (isFocused) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant
    )
    val indicatorHeight = animateDpAsState(
        targetValue = if (isFocused) 3.dp else 1.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium)
    )
    BasicTextField(
        modifier = modifier
            .onFocusChanged {
                isFocused = it.hasFocus
            },
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        enabled = enabled,
        readOnly = readOnly,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                        if (leadingIcon != null) {
                            leadingIcon.invoke()
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                        if (prefix != null) {
                            ProvideTextStyle(
                                value = MaterialTheme.typography.bodyLarge
                            ) {
                                prefix.invoke()
                            }
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            if (placeholderText != null && value.isEmpty()) {
                                Text(
                                    text = labelText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    minLines = if (singleLine) 1 else minLines,
                                    maxLines = if (singleLine) 1 else maxLines,
                                    overflow = TextOverflow.Ellipsis

                                )
                            }
                            innerTextField.invoke()
                        }
                        if (suffix != null) {
                            ProvideTextStyle(
                                value = MaterialTheme.typography.bodyLarge
                            ) {
                                suffix.invoke()
                            }
                        }
                        if (trailingIcon != null) {
                            Spacer(modifier = Modifier.width(16.dp))
                            trailingIcon.invoke()
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(color = Color.Transparent)
                ) {
                    Box(modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(indicatorHeight.value)
                        .background(color = indicatorColor.value)
                    )
                }
            }
        }
    )
}

@Composable
fun FloweryFilledTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        minLines = minLines,
        maxLines = maxLines,
        singleLine = singleLine,
        enabled = enabled,
        readOnly = readOnly,
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        shape = MaterialTheme.shapes.small.copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0f)
        )
    )
}