package com.glitchcode.flowery.login.presentation

data class LoginScreenState(
    val loginType: LoginType = LoginType.PHONE,
    val isRequiredVerificationCode: Boolean = false,
    val isLoggingIn: Boolean = false
)

enum class LoginType {
    PHONE, PASSWORD, NEW_ACCOUNT
}
