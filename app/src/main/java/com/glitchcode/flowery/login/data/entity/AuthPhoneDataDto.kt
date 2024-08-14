package com.glitchcode.flowery.login.data.entity

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AuthPhoneDataDto(
    val phone: String,
    val code: String?
)
