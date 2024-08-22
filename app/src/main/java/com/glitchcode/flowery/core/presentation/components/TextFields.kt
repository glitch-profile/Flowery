package com.glitchcode.flowery.core.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun FloweryTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanges: (String) -> Unit,
    titleText: String,
    labelText: String,
    charactersLimit: Int? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
) {
    val placeHolderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)

    var isFocused by remember {
        mutableStateOf(false)
    }
    BasicTextField(
        modifier = modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        value = value,
        onValueChange = { newValue ->
            if (charactersLimit != null) {
                onValueChanges.invoke(newValue.take(charactersLimit))
            } else onValueChanges.invoke(newValue)
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
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = modifier.weight(1f)
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = labelText,
                                style = MaterialTheme.typography.bodyLarge,
                                color = placeHolderColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField.invoke()
                    }
                    if (charactersLimit != null) {
                        val remainingCharactersCountColor = animateColorAsState(
                            targetValue = if (charactersLimit <= value.length) MaterialTheme.colorScheme.error
                            else placeHolderColor,
                            label = "remainingCharactersCountColorChange"
                        )

                        Spacer(modifier = Modifier.width(8.dp))
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