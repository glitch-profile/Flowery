package com.glitchcode.flowery.login.domain.repository

import com.glitchcode.flowery.core.domain.utils.Resource
import com.glitchcode.flowery.login.data.entity.AuthResponseDto

interface RemoteAuthRepository {

    suspend fun loginByPhone(
        phoneNumber: String
    ): Resource<Unit>

    suspend fun confirmPhoneNumber(
        phoneNumber: String,
        verificationCode: String
    ): Resource<String>

    suspend fun loginByPassword(
        username: String,
        password: String
    ): Resource<String>

    suspend fun registerNewPhone(
        phoneNumber: String
    ): Resource<Unit>

    suspend fun registerNewAccount(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        verificationCode: String
    ): Resource<String>

    suspend fun registerSession(
        sessionId: String
    ): Resource<AuthResponseDto>

//    suspend fun loginAsGuest(): Resource<AuthResponseDto>

    suspend fun checkSessionStatus(
        sessionId: String
    ): Resource<Unit>

    suspend fun updateAuthInfo(
        sessionId: String
    ): Resource<AuthResponseDto>

    suspend fun logout(
        sessionId: String
    ): Resource<Unit>

}