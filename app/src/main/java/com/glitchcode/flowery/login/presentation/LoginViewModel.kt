package com.glitchcode.flowery.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glitchcode.flowery.R
import com.glitchcode.flowery.core.domain.repository.LocalAuthDataRepository
import com.glitchcode.flowery.core.domain.utils.Resource
import com.glitchcode.flowery.core.presentation.components.notification.SwipeableNotificationState
import com.glitchcode.flowery.login.domain.usecases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val localAuthDataRepository: LocalAuthDataRepository,
    private val authUseCase: AuthUseCase
): ViewModel() {

    private val _loginState = MutableStateFlow(LoginScreenState())
    val loginState = _loginState.asStateFlow()

    private val _userPhoneNumber = MutableStateFlow("")
    val phoneNumber = _userPhoneNumber.asStateFlow()
    private val _phoneVerificationCode = MutableStateFlow("")
    val phoneVerificationCode = _phoneVerificationCode.asStateFlow()
    private val _userLogin = MutableStateFlow("")
    val userLogin = _userLogin.asStateFlow()
    private val _userPassword = MutableStateFlow("")
    val userPassword = _userPassword.asStateFlow()

    val notificationState = SwipeableNotificationState()

    init {
        val savedLogin = localAuthDataRepository.getSavedEmployeeLogin() ?: ""
        updateLoginText(savedLogin)
    }

    fun updatePhoneNumberText(phone: String) {
        val formattedPhone = phone.filter { it.isDigit() }
        if (formattedPhone.length <= 10) _userPhoneNumber.update { formattedPhone }
    }
    fun updateVerificationCodeText(code: String) {
        val formattedCode = code.filter { it.isDigit() }
        if (formattedCode.length <= 5) _phoneVerificationCode.update { formattedCode }
    }
    fun updateLoginText(login: String) {
        if (login.length <= 20) _userLogin.update { login }
    }
    fun updatePasswordText(password: String) {
        if (password.length <= 20) _userPassword.update { password }
    }
    fun updateLoginType(loginType: LoginType) {
        _loginState.update {
            it.copy(loginType = loginType)
        }
    }
    fun updateIsRequestingCode(status: Boolean) {
        _loginState.update {
            it.copy(isRequiredVerificationCode = status)
        }
    }
    fun showNotification(
        titleRes: Int = R.string.login_screen_error_notification_title_text,
        textRes: Int
    ) {
        notificationState.showNotification(
            titleRes = titleRes,
            textRes = textRes
        )
    }

    fun requestVerificationCode() {
        viewModelScope.launch {
            val phone = "+7" + phoneNumber.value
            val loginResult = authUseCase.loginByPhone(phone)
            if (loginResult is Resource.Success) {
                _loginState.update { it.copy(isRequiredVerificationCode = true) }
            } else {
                showNotification(
                    textRes = loginResult.messageRes!!
                )
            }
        }
    }

    fun loginByPhone(
        onLoginComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val loginResult = authUseCase.confirmPhone(
                phoneNumber = phoneNumber.value,
                verificationCode = phoneVerificationCode.value
            )
            if (loginResult is Resource.Success) {
                registerSession {
                    onLoginComplete.invoke()
                }
            } else showNotification(
                textRes = loginResult.messageRes!!
            )
        }
    }

    fun loginByPassword(
        onLoginComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val loginResult = authUseCase.loginPassword(
                login = userLogin.value,
                password = userPassword.value
            )
            if (loginResult is Resource.Success) {
                registerSession {
                    onLoginComplete.invoke()
                }
            } else showNotification(
                textRes = loginResult.messageRes!!
            )
        }
    }

    private suspend fun registerSession(onSessionRegistered: () -> Unit) {
        val registerSessionResult = authUseCase.registerSession()
        if (registerSessionResult is Resource.Success) {
            onSessionRegistered.invoke()
        } else showNotification(
            textRes = registerSessionResult.messageRes!!
        )
    }
}