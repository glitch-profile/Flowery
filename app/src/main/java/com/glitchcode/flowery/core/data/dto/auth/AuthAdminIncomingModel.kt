package com.glitchcode.flowery.core.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
class AuthAdminIncomingModel(
    val username: String,
    val password: String
)