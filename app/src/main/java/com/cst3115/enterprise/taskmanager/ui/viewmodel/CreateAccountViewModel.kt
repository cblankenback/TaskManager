package com.cst3115.enterprise.taskmanager.ui.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cst3115.enterprise.taskmanager.model.Availability
import com.cst3115.enterprise.taskmanager.model.Department
import com.cst3115.enterprise.taskmanager.model.EmployeeRequestDTO
import com.cst3115.enterprise.taskmanager.model.EmployeeResponseDTO
import com.cst3115.enterprise.taskmanager.model.Role
import com.cst3115.enterprise.taskmanager.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for handling account creation and fetching related data.
 *
 * @param application The application context.
 */
class CreateAccountViewModel(application: Application) : AndroidViewModel(application) {

    // Context from Application
    private val context: Context = getApplication<Application>().applicationContext

    // Retrofit API Service
    private val apiService = RetrofitInstance.getApiService(context)

    // Departments
    private val _departments = MutableStateFlow<List<Department>>(emptyList())
    val departments: StateFlow<List<Department>> = _departments

    private val _isLoadingDepartments = MutableStateFlow(true)
    val isLoadingDepartments: StateFlow<Boolean> = _isLoadingDepartments

    // Availabilities
    private val _availabilities = MutableStateFlow<List<Availability>>(emptyList())
    val availabilities: StateFlow<List<Availability>> = _availabilities

    private val _isLoadingAvailabilities = MutableStateFlow(true)
    val isLoadingAvailabilities: StateFlow<Boolean> = _isLoadingAvailabilities

    // Roles
    private val _roles = MutableStateFlow<List<Role>>(emptyList())
    val roles: StateFlow<List<Role>> = _roles

    private val _isLoadingRoles = MutableStateFlow(true)
    val isLoadingRoles: StateFlow<Boolean> = _isLoadingRoles

    // Registration State
    private val _isRegistering = MutableStateFlow(false)
    val isRegistering: StateFlow<Boolean> = _isRegistering

    // Error Message
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchDepartments()
        fetchAvailabilities()
        fetchRoles()
    }

    /**
     * Fetches the list of departments from the backend API.
     */
    private fun fetchDepartments() {
        viewModelScope.launch {
            try {
                val response = apiService.getDepartments()
                if (response.isSuccessful) {
                    _departments.value = response.body() ?: emptyList()
                    Log.d("CreateAccountViewModel", "Departments loaded: ${_departments.value}")
                } else {
                    _errorMessage.value = "Failed to load departments."
                    Log.e("CreateAccountViewModel", "Failed to load departments: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading departments: ${e.localizedMessage}"
                Log.e("CreateAccountViewModel", "Error loading departments", e)
            } finally {
                _isLoadingDepartments.value = false
            }
        }
    }

    /**
     * Fetches the list of availabilities from the backend API.
     */
    private fun fetchAvailabilities() {
        viewModelScope.launch {
            try {
                val response = apiService.getAvailabilities()
                if (response.isSuccessful) {
                    _availabilities.value = response.body() ?: emptyList()
                    Log.d("CreateAccountViewModel", "Availabilities loaded: ${_availabilities.value}")
                } else {
                    _errorMessage.value = "Failed to load availabilities."
                    Log.e("CreateAccountViewModel", "Failed to load availabilities: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading availabilities: ${e.localizedMessage}"
                Log.e("CreateAccountViewModel", "Error loading availabilities", e)
            } finally {
                _isLoadingAvailabilities.value = false
            }
        }
    }

    /**
     * Fetches the list of roles from the backend API.
     */
    private fun fetchRoles() {
        viewModelScope.launch {
            try {
                val response = apiService.getRoles()
                if (response.isSuccessful) {
                    _roles.value = response.body() ?: emptyList()
                    Log.d("CreateAccountViewModel", "Roles loaded: ${_roles.value}")
                } else {
                    _errorMessage.value = "Failed to load roles."
                    Log.e("CreateAccountViewModel", "Failed to load roles: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading roles: ${e.localizedMessage}"
                Log.e("CreateAccountViewModel", "Error loading roles", e)
            } finally {
                _isLoadingRoles.value = false
            }
        }
    }

    /**
     * Registers a new user by sending their details to the backend API.
     *
     * @param employeeRequest The user registration details.
     * @param onSuccess Callback invoked upon successful registration.
     */
    fun registerUser(employeeRequest: EmployeeRequestDTO, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isRegistering.value = true
            _errorMessage.value = null
            try {
                val response = apiService.registerUser(employeeRequest)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("CreateAccountViewModel", "Registration successful: $responseBody")
                    onSuccess()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Registration failed."
                    _errorMessage.value = errorMsg
                    Log.e("CreateAccountViewModel", "Registration failed: $errorMsg")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error during registration: ${e.localizedMessage}"
                Log.e("CreateAccountViewModel", "Error during registration", e)
            } finally {
                _isRegistering.value = false
            }
        }
    }

    /**
     * Sets an error message to be displayed in the UI.
     *
     * @param message The error message.
     */
    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

    /**
     * Clears any existing error messages.
     */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
