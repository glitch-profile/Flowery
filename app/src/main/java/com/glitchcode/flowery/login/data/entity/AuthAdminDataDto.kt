package com.glitchcode.flowery.login.data.entity

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
class AuthAdminDataDto(
    val username: String,
    val password: String
)