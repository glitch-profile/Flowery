package com.glitchcode.flowery.login.domain.usecases

import com.glitchcode.flowery.R
import com.glitchcode.flowery.core.domain.repository.LocalAuthDataRepository
import com.glitchcode.flowery.core.domain.repository.LocalNotificationsSettingsRepository
import com.glitchcode.flowery.core.domain.utils.EmployeeRoles
import com.glitchcode.flowery.core.domain.utils.Resource
import com.glitchcode.flowery.login.data.entity.AuthResponseDto
import com.glitchcode.flowery.login.domain.repository.RemoteAuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val apiAuthRepository: RemoteAuthRepository,
    private val localAuthRepository: LocalAuthDataRepository,
    private val localNotificationsSettings: LocalNotificationsSettingsRepository
    // TODO: Add notifications setup
) {

    suspend fun sendVerificationCode(
        phoneNumber: String,
        isNewAccount: Boolean
    ): Resource<Unit> {
        if (phoneNumber.isBlank()) {
            return Resource.Error(
                messageRes = R.string.login_screen_error_empty_phone_field,
                message = "Phone is empty."
            )
        }
        val phone = "+7$phoneNumber"
        println(phone)
        if (!Regex("^\\+7\\d{10}\$").matches(phone)) {
            return Resource.Error(
                message = "Incorrect phone number",
                messageRes = R.string.api_response_code_phone_incorrect
            )
        }
        val result = if (isNewAccount) apiAuthRepository.registerNewPhone(phone)
        else apiAuthRepository.loginByPhone(phone)
        return result
    }

    suspend fun loginPhone(
        phoneNumber: String,
        verificationCode: String
    ): Resource<Unit> {
        if (phoneNumber.isBlank()) {
            return Resource.Error(
                messageRes = R.string.login_screen_error_empty_phone_field,
                message = "Phone is empty."
            )
        }
        val phone = "+7$phoneNumber"
        if (!Regex("^\\+7\\d{10}\$").matches(phone)) {
            return Resource.Error(
                message = "Incorrect phone number",
                messageRes = R.string.api_response_code_phone_incorrect
            )
        }
        val result = apiAuthRepository.confirmPhoneNumber(
            phoneNumber = phone,
            verificationCode = verificationCode
        )
        if (result is Resource.Success) {
            localAuthRepository.setUserSessionId(result.data!!)
            val registerSessionResult = registerSession()
            return registerSessionResult
        } else return Resource.Error(
            messageRes = result.messageRes!!,
            message = result.message
        )
    }

    suspend fun loginPassword(
        login: String,
        password: String
    ): Resource<Unit> {
        if (login.isBlank() || password.isBlank()) {
            return Resource.Error(
                messageRes = R.string.login_screen_error_empty_fields,
                message = "Fields are empty."
            )
        }
        val result = apiAuthRepository.loginByPassword(
            username = login,
            password = password
        )
        if (result is Resource.Success) {
            localAuthRepository.setSavedEmployeeLogin(login)
            localAuthRepository.setUserSessionId(result.data!!)
            val registerSessionResult = registerSession()
            return registerSessionResult
        } else return Resource.Error(
            messageRes = result.messageRes!!,
            message = result.message
        )
    }

    suspend fun signIn(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        verificationCode: String
    ): Resource<Unit> {
        if (
            firstName.isBlank() || lastName.isBlank()
            || phoneNumber.isBlank()
//            || verificationCode.isBlank()
        ) {
            return Resource.Error(
                messageRes = R.string.login_screen_error_empty_fields,
                message = "Fields are empty."
            )
        }
        val phone = "+7$phoneNumber"
        if (!Regex("^\\+7\\d{10}\$").matches(phone)) {
            return Resource.Error(
                message = "Incorrect phone number",
                messageRes = R.string.api_response_code_phone_incorrect
            )
        }
        val result = apiAuthRepository.registerNewAccount(
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phone,
            verificationCode = verificationCode
        )
        if (result is Resource.Success) {
            localAuthRepository.setUserSessionId(result.data!!)
            val registerSessionResult = registerSession()
            return registerSessionResult
        } else return Resource.Error(
            messageRes = result.messageRes!!,
            message = result.message
        )
    }

    private suspend fun registerSession(): Resource<Unit> {
        val sessionId = localAuthRepository.getUserSessionId() ?: kotlin.run {
            return Resource.Error(
                message = "Session not found.",
                messageRes = R.string.api_response_code_session_not_found
            )
        }
        val result = apiAuthRepository.registerSession(sessionId)
        if (result is Resource.Success) {
            val authData = result.data!!
            localAuthRepository.setLoggedPersonId(authData.personId)
            localAuthRepository.setLoggedClientId(authData.clientId)
            localAuthRepository.setLoggedEmployeeId(authData.employeeId)
            localAuthRepository.setEmployeeRoles(authData.employeeRoles)
            return Resource.Success(Unit)
        } else return Resource.Error(
            messageRes = result.messageRes!!,
            message = result.message
        )
    }

    suspend fun updateAuthInfo(): Resource<AuthResponseDto> {
        val sessionId = localAuthRepository.getUserSessionId() ?: kotlin.run {
            return Resource.Error(
                message = "Session not found.",
                messageRes = R.string.api_response_code_session_not_found
            )
        }
        val result = apiAuthRepository.updateAuthInfo(sessionId)
        if (result is Resource.Success) {
            val authData = result.data!!
            if (authData.personId != localAuthRepository.getLoggedPersonId())
                localAuthRepository.setLoggedPersonId(authData.personId)
            if (authData.clientId != localAuthRepository.getLoggedClientId())
                localAuthRepository.setLoggedClientId(authData.clientId)
            if (authData.employeeId != localAuthRepository.getLoggedEmployeeId())
                localAuthRepository.setLoggedEmployeeId(authData.employeeId)
            val newEmployeeRoles = authData.employeeRoles.map { EmployeeRoles.valueOf(it) }
            if (newEmployeeRoles != localAuthRepository.getEmployeeRoles())
                localAuthRepository.setEmployeeRoles(authData.employeeRoles)
        }
        return result
    }

    suspend fun logout(): Resource<Unit> {
        val sessionId = localAuthRepository.getUserSessionId() ?: kotlin.run {
            return Resource.Error(
                message = "Session not found.",
                messageRes = R.string.api_response_code_session_not_found
            )
        }
        val result = apiAuthRepository.logout(sessionId)
        if (result is Resource.Success) {
            localAuthRepository.setLoggedPersonId(null)
            localAuthRepository.setLoggedClientId(null)
            localAuthRepository.setLoggedEmployeeId(null)
            localAuthRepository.setEmployeeRoles(emptyList())
        }
        return result
    }

}