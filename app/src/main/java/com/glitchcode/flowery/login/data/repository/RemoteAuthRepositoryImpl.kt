package com.glitchcode.flowery.login.data.repository

import com.glitchcode.flowery.core.data.entity.ApiResponseDto
import com.glitchcode.flowery.core.domain.utils.ApiResponseMessageCode
import com.glitchcode.flowery.core.domain.utils.Resource
import com.glitchcode.flowery.login.data.entity.AuthNewUserDataDto
import com.glitchcode.flowery.login.data.entity.AuthPasswordDataDto
import com.glitchcode.flowery.login.data.entity.AuthPhoneDataDto
import com.glitchcode.flowery.login.data.entity.AuthResponseDto
import com.glitchcode.flowery.login.domain.repository.RemoteAuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

private const val PATH = "/apiV1/auth"
private const val AUTH_SESSION_KEY = "auth_session"

class RemoteAuthRepositoryImpl(
    private val client: HttpClient
): RemoteAuthRepository {

    override suspend fun loginByPhone(phoneNumber: String): Resource<Unit> {
        return try {
            val response: ApiResponseDto<Unit> = client.post("$PATH/verify-phone-number") {
                setBody(phoneNumber)
                contentType(ContentType.Application.Json)
            }.body()
            if (response.status) {
                Resource.Success(data = Unit)
            } else {
                Resource.Error(
                    messageRes = ApiResponseMessageCode.getMessageRes(response.messageCode),
                    message = response.message
                )
            }
        } catch (e: Exception) {
            Resource.getResourceFromException(e)
        }
    }

    override suspend fun confirmPhoneNumber(
        phoneNumber: String,
        verificationCode: String
    ): Resource<String> {
        return try {
            val authRequestData = AuthPhoneDataDto(
                phone = phoneNumber,
                code = verificationCode
            )
            val responseInfo = client.post("$PATH/login") {
                setBody(authRequestData)
                contentType(ContentType.Application.Json)
            }
            val body = responseInfo.body<ApiResponseDto<Unit>>()
            if (body.status) {
                val sessionInfo = responseInfo.headers[AUTH_SESSION_KEY]!!
                Resource.Success(data = sessionInfo)
            } else {
                Resource.Error(
                    message = body.message,
                    messageRes = ApiResponseMessageCode.getMessageRes(body.messageCode)
                )
            }
        } catch (e: Exception) {
            Resource.getResourceFromException(e)
        }
    }

    override suspend fun loginByPassword(username: String, password: String): Resource<String> {
        return try {
            val authPasswordData = AuthPasswordDataDto(
                username = username,
                password = password
            )
            val responseInfo = client.post("$PATH/login-password") {
                setBody(authPasswordData)
                contentType(ContentType.Application.Json)
            }
            val body = responseInfo.body<ApiResponseDto<Unit>>()
            if (body.status) {
                val sessionInfo = responseInfo.headers[AUTH_SESSION_KEY]!!
                Resource.Success(data = sessionInfo)
            } else {
                Resource.Error(
                    message = body.message,
                    messageRes = ApiResponseMessageCode.getMessageRes(body.messageCode)
                )
            }
        } catch (e: Exception) {
            Resource.getResourceFromException(e)
        }
    }

    override suspend fun registerNewPhone(phoneNumber: String): Resource<Unit> {
        return try {
            val response: ApiResponseDto<Unit> = client.post("$PATH/verify-new-phone-number") {
                setBody(phoneNumber)
                contentType(ContentType.Application.Json)
            }.body()
            if (response.status) {
                Resource.Success(data = Unit)
            } else {
                Resource.Error(
                    messageRes = ApiResponseMessageCode.getMessageRes(response.messageCode),
                    message = response.message
                )
            }
        } catch (e: Exception) {
            Resource.getResourceFromException(e)
        }
    }

    override suspend fun registerNewAccount(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        verificationCode: String
    ): Resource<String> {
        return try {
            val newUserData = AuthNewUserDataDto(
                firstName = firstName,
                lastName = lastName,
                phone = phoneNumber,
                verificationCode = verificationCode
            )
            val responseInfo = client.post("$PATH/sign-in") {
                setBody(newUserData)
                contentType(ContentType.Application.Json)
            }
            val body = responseInfo.body<ApiResponseDto<Unit>>()
            if (body.status) {
                val sessionInfo = responseInfo.headers[AUTH_SESSION_KEY]!!
                Resource.Success(data = sessionInfo)
            } else {
                Resource.Error(
                    message = body.message,
                    messageRes = ApiResponseMessageCode.getMessageRes(body.messageCode)
                )
            }
        } catch (e: Exception) {
            Resource.getResourceFromException(e)
        }
    }

    override suspend fun registerSession(sessionId: String): Resource<AuthResponseDto> {
        return try {
            val response: ApiResponseDto<AuthResponseDto> = client.post("$PATH/session-verification") {
                header(AUTH_SESSION_KEY, sessionId)
            }. body()
            if (response.status) {
                Resource.Success(data = response.data)
            } else Resource.Error(
                message = response.message,
                messageRes = ApiResponseMessageCode.getMessageRes(response.messageCode)
            )
        } catch (e: Exception) {
            Resource.getResourceFromException(e)
        }
    }

    override suspend fun checkSessionStatus(sessionId: String): Resource<Unit> {
        return try {
            val response: ApiResponseDto<Unit> = client.get("$PATH/check-session") {
                header(AUTH_SESSION_KEY, sessionId)
            }.body()
            if (response.status) {
                Resource.Success(data = Unit)
            } else {
                Resource.Error(
                    message = response.message,
                    messageRes = ApiResponseMessageCode.getMessageRes(response.messageCode)
                )
            }
        } catch (e: Exception) {
            Resource.getResourceFromException(e)
        }
    }

    override suspend fun updateAuthInfo(sessionId: String): Resource<AuthResponseDto> {
        return try {
            val response: ApiResponseDto<AuthResponseDto?> = client.get("$PATH/update-auth-info") {
                header(AUTH_SESSION_KEY, sessionId)
            }.body()
            if (response.status) {
                Resource.Success(data = response.data!!)
            } else {
                Resource.Error(
                    message = response.message,
                    messageRes = ApiResponseMessageCode.getMessageRes(response.messageCode)
                )
            }
        } catch (e: Exception) {
            println(e)
            Resource.getResourceFromException(e)
        }
    }

    override suspend fun logout(sessionId: String): Resource<Unit> {
        return try {
            val response: ApiResponseDto<Unit> = client.post("$PATH/logout") {
                header(AUTH_SESSION_KEY, sessionId)
            }.body()
            if (response.status) {
                Resource.Success(data = Unit)
            } else {
                Resource.Error(
                    message = response.message,
                    messageRes = ApiResponseMessageCode.getMessageRes(response.messageCode)
                )
            }
        } catch (e: Exception) {
            Resource.getResourceFromException(e)
        }
    }
}