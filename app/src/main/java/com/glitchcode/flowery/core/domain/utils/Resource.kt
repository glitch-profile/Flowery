package com.glitchcode.flowery.core.domain.utils

import androidx.annotation.StringRes
import com.glitchcode.flowery.R
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    @StringRes val messageRes: Int? = null
) {
    class Success<T>(
        data: T,
        message: String? = null,
        @StringRes messageRes: Int? = null
    ): Resource<T>(
        data = data,
        message = message,
        messageRes = messageRes
    )
    class Error<T>(
        data: T? = null,
        message: String? = null,
        @StringRes messageRes: Int
    ): Resource<T>(
        data = data,
        message = message,
        messageRes = messageRes
    )

    companion object {
        fun <T>getResourceFromException(e: Exception): Error<T> {
            return when (e) {
                is ResponseException -> {
                    when (e.response.status) {
                        HttpStatusCode.Unauthorized -> Error(messageRes = R.string.api_response_code_unauthorized)
                        HttpStatusCode.Forbidden -> Error(messageRes = R.string.api_response_code_forbidden)
                        HttpStatusCode.BadRequest -> Error(messageRes = R.string.api_response_code_bad_request)
                        HttpStatusCode.NotFound -> Error(messageRes = R.string.api_response_code_not_found)
                        else -> Error(messageRes = R.string.api_response_code_unknown)
                    }
                }
                is ConnectTimeoutException -> Error(messageRes = R.string.api_response_code_connect_timeout)
                else -> Error(messageRes = R.string.api_response_code_unknown)
            }
        }
    }
}
