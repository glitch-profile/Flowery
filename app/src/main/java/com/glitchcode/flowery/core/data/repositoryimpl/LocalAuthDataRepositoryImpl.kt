package com.glitchcode.flowery.core.data.repositoryimpl

import android.content.Context
import android.content.SharedPreferences
import com.glitchcode.flowery.core.domain.repository.LocalAuthDataRepository
import com.glitchcode.flowery.core.domain.utils.EmployeeRoles
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val PREFERENCES_NAME = "AuthDataPreferences"
private const val SAVED_EMPLOYEE_LOGIN = "savedEmployeeLogin"
private const val SAVED_USER_SESSION_ID = "savedUserSessionId"
private const val LOGGED_PERSON_ID = "loggedPersonId"
private const val LOGGED_CLIENT_ID = "loggedClientId"
private const val LOGGED_EMPLOYEE_ID = "loggedEmployeeId"
private const val LOGGED_EMPLOYEE_ROLES = "loggedEmployeeRoles"

class LocalAuthDataRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): LocalAuthDataRepository {

    override val preferences: SharedPreferences
        get() = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private var savedEmployeeLogin: String? = null
    override fun getSavedEmployeeLogin(): String? {
        return savedEmployeeLogin ?: kotlin.run {
            savedEmployeeLogin = preferences.getString(SAVED_EMPLOYEE_LOGIN, null)
            savedEmployeeLogin
        }
    }
    override fun setSavedEmployeeLogin(value: String?) {
        savedEmployeeLogin = value
        preferences.edit().putString(SAVED_EMPLOYEE_LOGIN, value).apply()
    }

    private var savedSessionId: String? = null
    override fun getUserSessionId(): String? {
        return savedSessionId ?: kotlin.run {
            savedSessionId = preferences.getString(SAVED_USER_SESSION_ID, null)
            savedSessionId
        }
    }
    override fun setUserSessionId(value: String?) {
        savedSessionId = value
        preferences.edit().putString(SAVED_USER_SESSION_ID, value).apply()
    }

    private var savedPersonId: String? = null
    override fun getLoggedPersonId(): String? {
        return savedPersonId ?: kotlin.run {
            savedPersonId = preferences.getString(LOGGED_PERSON_ID, null)
            savedPersonId
        }
    }
    override fun setLoggedPersonId(value: String?) {
        savedPersonId = value
        preferences.edit().putString(LOGGED_PERSON_ID, value).apply()
    }

    private var savedClientId: String? = null
    override fun getLoggedClientId(): String? {
        return savedClientId ?: kotlin.run {
            savedClientId = preferences.getString(LOGGED_CLIENT_ID, null)
            savedClientId
        }
    }
    override fun setLoggedClientId(value: String?) {
        savedClientId = value
        preferences.edit().putString(LOGGED_CLIENT_ID, value).apply()
    }

    private var savedEmployeeId: String? = null
    override fun getLoggedEmployeeId(): String? {
        return savedEmployeeId ?: kotlin.run {
            savedEmployeeId = preferences.getString(LOGGED_EMPLOYEE_ID, null)
            savedEmployeeId
        }
    }
    override fun setLoggedEmployeeId(value: String?) {
        savedEmployeeId = value
        preferences.edit().putString(LOGGED_EMPLOYEE_ID, value).apply()
    }

    private var employeeRoles: List<EmployeeRoles>? = null
    override fun getEmployeeRoles(): List<EmployeeRoles> {
        return employeeRoles ?: kotlin.run {
            val savedRoles = preferences.getStringSet(LOGGED_EMPLOYEE_ROLES, emptySet()) ?: emptySet()
            employeeRoles = savedRoles.toList().map { EmployeeRoles.valueOf(it) }
            employeeRoles!!
        }
    }
    override fun getIsHasSpecificRole(role: EmployeeRoles): Boolean {
        val roles = getEmployeeRoles()
        return roles.contains(role)
    }
    override fun setEmployeeRoles(list: List<String>) {
        employeeRoles = list.map { EmployeeRoles.valueOf(it) }
        preferences.edit().putStringSet(LOGGED_EMPLOYEE_ROLES, list.toSet()).apply()
    }
}