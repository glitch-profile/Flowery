package com.glitchcode.flowery.core.domain.utils

import androidx.annotation.StringRes
import com.glitchcode.flowery.R

enum class ApiResponseMessageCode() {
    OK,
    UNKNOWN,
    //general errors
    USER_NOT_FOUND,
    //sessions errors
    SESSION_NOT_FOUND,
    //login errors
    AUTH_DATA_INCORRECT,
    PHONE_NOT_FOUND,
    PHONE_INCORRECT,
    CODE_INCORRECT;

    companion object {
        @StringRes fun getMessageRes(errorCode: String): Int {
            val errorType = try {
                ApiResponseMessageCode.valueOf(errorCode)
            } catch (e: IllegalArgumentException) {
                UNKNOWN
            }
            return when (errorType) {
                OK -> R.string.api_response_code_ok
                UNKNOWN -> R.string.api_response_code_unknown
                USER_NOT_FOUND -> R.string.api_response_code_user_not_found
                SESSION_NOT_FOUND -> R.string.api_response_code_session_not_found
                AUTH_DATA_INCORRECT -> R.string.api_response_code_auth_data_incorrect
                PHONE_NOT_FOUND -> R.string.api_response_code_phone_not_found
                PHONE_INCORRECT -> R.string.api_response_code_phone_incorrect
                CODE_INCORRECT -> R.string.api_response_code_code_incorrect
            }
        }
    }
}

