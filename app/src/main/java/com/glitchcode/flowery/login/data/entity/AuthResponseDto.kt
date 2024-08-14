package com.glitchcode.flowery.login.data.entity

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AuthResponseDto(
    val personId: String,
    val clientId: String?,
    val employeeId: String?,
    val employeeRoles: List<String>
)
