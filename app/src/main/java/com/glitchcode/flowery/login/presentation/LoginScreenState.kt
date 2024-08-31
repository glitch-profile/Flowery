package com.glitchcode.flowery.login.presentation

data class LoginScreenState(
    val loginType: LoginType = LoginType.PHONE,
    val isLoadingCode: Boolean = false,
    val isNewUserLoadingCode: Boolean = false,
    val isWaitingCodeInput: Boolean = false,
    val isNewUserWaitingCodeInput: Boolean = false,
    val isLoggingIn: Boolean = false
)

enum class LoginType {
    PHONE, PASSWORD, NEW_ACCOUNT
}
