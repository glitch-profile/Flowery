package com.glitchcode.flowery.login.data.entity

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class AuthNewUserDataDto(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val verificationCode: String
)
