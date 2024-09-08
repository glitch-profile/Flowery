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

    private val _newUserFirstName = MutableStateFlow("")
    val newUserFirstName = _newUserFirstName.asStateFlow()
    private val _newUserLastName = MutableStateFlow("")
    val newUserLastName = _newUserLastName.asStateFlow()
    private val _newUserPhoneVerificationCode = MutableStateFlow("")
    val newUserPhoneVerificationCode = _newUserPhoneVerificationCode.asStateFlow()

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
    fun updateNewUserFirstName(firstName: String) {
        if (firstName.length <= 20) _newUserFirstName.update { firstName }
    }
    fun updateNewUserLastName(lastName: String) {
        if (lastName.length <= 20) _newUserLastName.update { lastName }
    }
    fun updateNewUserVerificationCode(code: String) {
        val formattedCode = code.filter { it.isDigit() }
        if (formattedCode.length <= 5) _newUserPhoneVerificationCode.update { code }
    }
    private fun showNotification(
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
            if (loginState.value.isLoadingCode) return@launch
            _loginState.update { it.copy(isLoadingCode = true) }
            val loginResult = authUseCase.sendVerificationCode(phoneNumber.value, isNewAccount = false)
            if (loginResult is Resource.Success) {
                _loginState.update { it.copy(isWaitingCodeInput = true) }
            } else {
                showNotification(
                    textRes = loginResult.messageRes!!
                )
            }
            _loginState.update { it.copy(isLoadingCode = false) }
        }
    }

    fun newUserRequestVerificationCode() {
        viewModelScope.launch {
            if (loginState.value.isNewUserLoadingCode) return@launch
            _loginState.update { it.copy(isNewUserLoadingCode = true) }
            val loginResult = authUseCase.sendVerificationCode(phoneNumber.value, isNewAccount = true)
            if (loginResult is Resource.Success) {
                _loginState.update { it.copy(isNewUserWaitingCodeInput = true) }
            } else {
                showNotification(
                    textRes = loginResult.messageRes!!
                )
            }
            _loginState.update { it.copy(isNewUserLoadingCode = false) }
        }
    }

    fun loginByPhone(
        onLoginComplete: () -> Unit
    ) {
        viewModelScope.launch {
            if (loginState.value.isLoggingIn) return@launch
            _loginState.update { it.copy(isLoggingIn = true) }
            val loginResult = authUseCase.loginPhone(
                phoneNumber = phoneNumber.value,
                verificationCode = phoneVerificationCode.value
            )
            if (loginResult is Resource.Success) {
                onLoginComplete.invoke()
            } else showNotification(
                textRes = loginResult.messageRes!!
            )
            _loginState.update { it.copy(isLoggingIn = false) }
        }
    }

    fun loginByPassword(
        onLoginComplete: () -> Unit
    ) {
        viewModelScope.launch {
            if (loginState.value.isLoggingIn) return@launch
            _loginState.update { it.copy(isLoggingIn = true) }
            val loginResult = authUseCase.loginPassword(
                login = userLogin.value,
                password = userPassword.value
            )
            if (loginResult is Resource.Success) {
                onLoginComplete.invoke()
            } else showNotification(
                textRes = loginResult.messageRes!!
            )
            _loginState.update { it.copy(isLoggingIn = false) }
        }
    }

    fun signIn(
        onSignInComplete: () -> Unit
    ) {
        viewModelScope.launch {
            if (loginState.value.isLoggingIn) return@launch
            _loginState.update { it.copy(isLoggingIn = true) }
            val signInResult = authUseCase.signIn(
                firstName = newUserFirstName.value,
                lastName = newUserLastName.value,
                phoneNumber = phoneNumber.value,
                verificationCode = newUserPhoneVerificationCode.value
            )
            if (signInResult is Resource.Success) {
                onSignInComplete.invoke()
            } else {
                showNotification(
                    textRes = signInResult.messageRes!!
                )
            }
            _loginState.update { it.copy(isLoggingIn = false) }
        }
    }
}