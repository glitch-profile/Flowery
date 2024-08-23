package com.glitchcode.flowery.login.domain.usecases

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
) {

    suspend fun loginByPhone(
        phoneNumber: String
    ): Resource<Unit> {
        if (!Regex("^\\+7\\d{10}\$").matches(phoneNumber)) {
            return Resource.Error(message = "incorrect phone number")
        }
        val result = apiAuthRepository.loginByPhone(phoneNumber)
        return result
    }

    suspend fun confirmPhone(
        phoneNumber: String,
        verificationCode: String
    ): Resource<String> {
        val result = apiAuthRepository.confirmPhoneNumber(
            phoneNumber = phoneNumber,
            verificationCode = verificationCode
        )
        if (result is Resource.Success) {
            localAuthRepository.setUserSessionId(result.data!!)
        }
        return result
    }

    suspend fun loginPassword(
        login: String,
        password: String
    ): Resource<String> {
        val result = apiAuthRepository.loginByPassword(
            username = login,
            password = password
        )
        if (result is Resource.Success) {
            localAuthRepository.setUserSessionId(result.data!!)
        }
        return result
    }

    suspend fun registerSession(): Resource<AuthResponseDto> {
        val sessionId = localAuthRepository.getUserSessionId() ?: kotlin.run {
            return Resource.Error(message = "session not found.")
        }
        val result = apiAuthRepository.registerSession(sessionId)
        if (result is Resource.Success) {
            val authData = result.data!!
            localAuthRepository.setLoggedPersonId(authData.personId)
            localAuthRepository.setLoggedClientId(authData.clientId)
            localAuthRepository.setLoggedEmployeeId(authData.employeeId)
            localAuthRepository.setEmployeeRoles(authData.employeeRoles)
        }
        return result
    }

    suspend fun updateAuthInfo(): Resource<AuthResponseDto> {
        val sessionId = localAuthRepository.getUserSessionId() ?: kotlin.run {
            return Resource.Error(message = "session not found.")
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
            return Resource.Error(message = "session not found.")
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