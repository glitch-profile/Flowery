package com.glitchcode.flowery.login.data.repository

import com.glitchcode.flowery.core.data.entity.ApiResponseDto
import com.glitchcode.flowery.core.domain.utils.Resource
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
import javax.inject.Inject
import javax.inject.Named

private const val PATH = "/apiV1/auth"
private const val AUTH_SESSION_KEY = "auth_session"

class RemoteAuthRepositoryImpl @Inject constructor(
    @Named("RestClient") private val client: HttpClient
): RemoteAuthRepository {

    override suspend fun loginByPhone(phoneNumber: String): Resource<Unit> {
        return try {
            val authRequestData = AuthPhoneDataDto(
                phone = phoneNumber,
                code = null
            )
            val response: ApiResponseDto<Unit> = client.post("$PATH/login-client") {
                setBody(authRequestData)
                contentType(ContentType.Application.Json)
            }.body()
            if (response.status) {
                Resource.Success(data = Unit)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (e: Exception) {
            Resource.Error(message = "unknown error")
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
            val responseInfo = client.post("$PATH/client-verification") {
                setBody(authRequestData)
                contentType(ContentType.Application.Json)
            }
            val body = responseInfo.body<ApiResponseDto<Unit>>()
            if (body.status) {
                val sessionInfo = responseInfo.headers[AUTH_SESSION_KEY]!!
                Resource.Success(data = sessionInfo)
            } else {
                Resource.Error(message = body.message)
            }
        } catch (e: Exception) {
            Resource.Error(message = "unknown error")
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
                Resource.Error(message = body.message)
            }
        } catch (e: Exception) {
            Resource.Error(message = "unknown error")
        }
    }

    override suspend fun registerSession(sessionId: String): Resource<AuthResponseDto> {
        return try {
            val response: ApiResponseDto<AuthResponseDto> = client.post("$PATH/session-verification") {
                header(AUTH_SESSION_KEY, sessionId)
            }. body()
            if (response.status) {
                Resource.Success(data = response.data)
            } else Resource.Error(message = response.message)
        } catch (e: Exception) {
            Resource.Error(message = "unknown error")
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
                Resource.Error(message = response.message)
            }
        } catch (e: Exception) {
            Resource.Error(message = "unknown error")
        }
    }

    override suspend fun updateAuthInfo(sessionId: String): Resource<AuthResponseDto> {
        return try {
            val response: ApiResponseDto<AuthResponseDto> = client.get("$PATH/update-auth-session") {
                header(AUTH_SESSION_KEY, sessionId)
            }.body()
            if (response.status) {
                Resource.Success(data = response.data)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (e: Exception) {
            Resource.Error(message = "unknown error")
        }
    }

    override suspend fun logout(sessionId: String): Resource<Unit> {
        return try {
            val response: ApiResponseDto<Unit> = client.get("$PATH/logout") {
                header(AUTH_SESSION_KEY, sessionId)
            }.body()
            if (response.status) {
                Resource.Success(data = Unit)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (e: Exception) {
            Resource.Error(message = "unknown error")
        }
    }
}