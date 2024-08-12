package com.glitchcode.flowery.core.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthPhoneIncomingModel(
    val phone: String,
    val code: String?
)
