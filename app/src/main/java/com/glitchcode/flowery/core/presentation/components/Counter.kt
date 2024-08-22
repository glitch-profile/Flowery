package com.glitchcode.flowery.core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun CounterDynamic(
    modifier: Modifier = Modifier,
    count: Int,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalTextStyle.current.color
) {
    var oldCount by remember {
        mutableIntStateOf(count)
    }
    SideEffect {
        oldCount = count
    }
    Row(modifier = modifier) {
        val oldText = oldCount.toString()
        val text = count.toString()
        for (i in 0..text.length) { // getting a small amount of extra text fields
            val oldChar = oldText.getOrNull(i) ?: ""
            val newChar = text.getOrNull(i) ?: ""
            val char = if (oldChar == newChar) {
                oldText.getOrNull(i) ?: ""
            } else {
                text.getOrNull(i) ?: ""
            }
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically(
                        animationSpec = spring(
                            stiffness = 1000f
                        )
                    ) { it } togetherWith slideOutVertically(
                        animationSpec = spring(
                            stiffness = 1000f
                        )
                    ) { -it }
                }
            ) { char ->
                Text(
                    text = char.toString(),
                    style = style,
                    color = color,
                    softWrap = false
                )
            }
        }
    }
}

@Composable
fun CounterFixed(
    modifier: Modifier = Modifier,
    count: Int,
    maxChars: Int = 3,
    invertDirection: Boolean = false,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalTextStyle.current.color
) {
    var oldCount by remember {
        mutableIntStateOf(count)
    }
    SideEffect {
        oldCount = count
    }
    Row(modifier = modifier) {
        val oldText = oldCount.toString()
        val text = count.toString()
        val normalizedOldText = if (invertDirection) {
            " ".repeat(maxChars - oldText.length) + oldText
        } else oldText + " ".repeat(maxChars - oldText.length)
        val normalizedNewText = if (invertDirection) {
            " ".repeat(maxChars - text.length) + text
        } else text + " ".repeat(maxChars - text.length)
        for (i in normalizedNewText.indices) {
            val oldChar = normalizedOldText.getOrNull(i)
            val newChar = normalizedNewText.getOrNull(i)
            val char = if (oldChar == newChar) {
                normalizedOldText.getOrNull(i) ?: ""
            } else {
                normalizedNewText.getOrNull(i) ?: ""
            }
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically(
                        animationSpec = spring(stiffness = 1000f)
                    ) { it } togetherWith slideOutVertically(
                        animationSpec = spring(stiffness = 1000f)
                    ) { -it }
                }
            ) { char ->
                Text(
                    text = char.toString(),
                    style = style,
                    color = color,
                    softWrap = false
                )
            }
        }
    }
}