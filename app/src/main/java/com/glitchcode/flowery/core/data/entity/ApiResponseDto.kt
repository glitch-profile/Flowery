package com.glitchcode.flowery.core.data.entity

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ApiResponseDto<T>(
    val status: Boolean,
    val message: String,
    val messageCode: String,
    val data: T
)
