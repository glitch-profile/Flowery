package com.glitchcode.flowery.core.domain.repository

import android.content.SharedPreferences
import com.glitchcode.flowery.core.domain.utils.EmployeeRoles

interface LocalAuthDataRepository {

    val preferences: SharedPreferences

    fun getSavedEmployeeLogin(): String?
    fun setSavedEmployeeLogin(value: String?)

    fun getUserSessionId(): String?
    fun setUserSessionId(value: String?)

    fun getLoggedPersonId(): String?
    fun setLoggedPersonId(value: String?)

    fun getLoggedClientId(): String?
    fun setLoggedClientId(value: String?)

    fun getLoggedEmployeeId(): String?
    fun setLoggedEmployeeId(value: String?)

    fun getEmployeeRoles(): List<EmployeeRoles>
    fun getIsHasSpecificRole(role: EmployeeRoles): Boolean
    fun setEmployeeRoles(list: List<String>)

}