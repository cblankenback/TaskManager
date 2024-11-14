// File: com/cst3115/enterprise/taskmanager/ui/viewmodel/UserViewModel.kt

package com.cst3115.enterprise.taskmanager.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cst3115.enterprise.taskmanager.model.UserDetails
import com.cst3115.enterprise.taskmanager.model.UserDetailsUpdateDTO
import com.cst3115.enterprise.taskmanager.network.RetrofitInstance
import com.cst3115.enterprise.taskmanager.util.TokenProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for fetching, storing, and updating the authenticated user's details.
 *
 * @param application The application context.
 */
class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val apiService = RetrofitInstance.getApiService(context)

    // StateFlow for User Details
    private val _userDetails = MutableStateFlow<UserDetails?>(null)
    val userDetails: StateFlow<UserDetails?> = _userDetails

    // StateFlow for Loading State
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // StateFlow for Error Messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // StateFlow for Updating State
    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating

    // StateFlow for Update Error Messages
    private val _updateErrorMessage = MutableStateFlow<String?>(null)
    val updateErrorMessage: StateFlow<String?> = _updateErrorMessage

    // Additional StateFlows for Dropdown Data
    private val _departments = MutableStateFlow<List<com.cst3115.enterprise.taskmanager.model.Department>>(emptyList())
    val departments: StateFlow<List<com.cst3115.enterprise.taskmanager.model.Department>> = _departments

    private val _isLoadingDepartments = MutableStateFlow(true)
    val isLoadingDepartments: StateFlow<Boolean> = _isLoadingDepartments

    private val _availabilities = MutableStateFlow<List<com.cst3115.enterprise.taskmanager.model.Availability>>(emptyList())
    val availabilities: StateFlow<List<com.cst3115.enterprise.taskmanager.model.Availability>> = _availabilities

    private val _isLoadingAvailabilities = MutableStateFlow(true)
    val isLoadingAvailabilities: StateFlow<Boolean> = _isLoadingAvailabilities

    private val _roles = MutableStateFlow<List<com.cst3115.enterprise.taskmanager.model.Role>>(emptyList())
    val roles: StateFlow<List<com.cst3115.enterprise.taskmanager.model.Role>> = _roles

    private val _isLoadingRoles = MutableStateFlow(true)
    val isLoadingRoles: StateFlow<Boolean> = _isLoadingRoles

    /**
     * Initializes the ViewModel by fetching necessary data.
     */
    init {
        fetchDepartments()
        fetchAvailabilities()
        fetchRoles()
        fetchCurrentUser()
    }

    /**
     * Fetches the list of departments from the backend API.
     */
    private fun fetchDepartments() {
        viewModelScope.launch {
            _isLoadingDepartments.value = true
            try {
                val response = apiService.getDepartments()
                if (response.isSuccessful) {
                    _departments.value = response.body() ?: emptyList()
                    Log.d("UserViewModel", "Departments fetched successfully: ${response.body()}")
                } else {
                    Log.e("UserViewModel", "Failed to fetch departments: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception fetching departments", e)
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
            _isLoadingAvailabilities.value = true
            try {
                val response = apiService.getAvailabilities()
                if (response.isSuccessful) {
                    _availabilities.value = response.body() ?: emptyList()
                    Log.d("UserViewModel", "Availabilities fetched successfully: ${response.body()}")
                } else {
                    Log.e("UserViewModel", "Failed to fetch availabilities: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception fetching availabilities", e)
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
            _isLoadingRoles.value = true
            try {
                val response = apiService.getRoles()
                if (response.isSuccessful) {
                    _roles.value = response.body() ?: emptyList()
                    Log.d("UserViewModel", "Roles fetched successfully: ${response.body()}")
                } else {
                    Log.e("UserViewModel", "Failed to fetch roles: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception fetching roles", e)
            } finally {
                _isLoadingRoles.value = false
            }
        }
    }

    /**
     * Fetches the current user's details from the backend.
     */
    private fun fetchCurrentUser() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = apiService.getCurrentUser() // Removed the "Bearer $token" argument
                if (response.isSuccessful) {
                    _userDetails.value = response.body()
                    Log.d("UserViewModel", "User details fetched successfully: ${response.body()}")
                } else {
                    _errorMessage.value = "Failed to fetch user details: ${response.message()}"
                    Log.e("UserViewModel", "Error fetching user details: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
                Log.e("UserViewModel", "Exception fetching user details", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Refreshes the user's details by fetching them again from the backend.
     */
    fun refreshUserDetails() {
        fetchCurrentUser()
    }

    /**
     * Updates the current user's details.
     *
     * @param firstName The updated first name.
     * @param lastName The updated last name.
     * @param departmentId The updated department ID.
     * @param availabilityId The updated availability ID.
     * @param roleId The updated role ID.
     * @param onSuccess Callback invoked upon successful update.
     * @param onError Callback invoked with an error message upon failure.
     */
    fun updateUserDetails(
        firstName: String,
        lastName: String,
        departmentId: Int,
        availabilityId: Int,
        roleId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isUpdating.value = true
            _updateErrorMessage.value = null
            try {
                val token = TokenProvider.getToken(context)
                if (token == null) {
                    _updateErrorMessage.value = "User is not authenticated."
                    Log.e("UserViewModel", "No JWT token found.")
                    onError("User is not authenticated.")
                    return@launch
                }

                val employeeId = _userDetails.value?.employeeId ?: run {
                    _updateErrorMessage.value = "User ID not found."
                    Log.e("UserViewModel", "User ID not found.")
                    onError("User ID not found.")
                    return@launch
                }

                val updateDTO = UserDetailsUpdateDTO(
                    firstName = firstName,
                    lastName = lastName,
                    departmentId = departmentId,
                    availabilityId = availabilityId,
                    roleId = roleId
                )

                val response = apiService.updateUserDetails(employeeId, updateDTO)
                if (response.isSuccessful) {
                    // Instead of assigning response.body(), refetch the user details
                    fetchCurrentUser()
                    Log.d("UserViewModel", "User details updated successfully.")
                    onSuccess()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Update failed."
                    _updateErrorMessage.value = errorMsg
                    onError(errorMsg)
                    Log.e("UserViewModel", "Update failed: $errorMsg")
                }
            } catch (e: Exception) {
                _updateErrorMessage.value = "Error updating details: ${e.localizedMessage}"
                onError("Error updating details: ${e.localizedMessage}")
                Log.e("UserViewModel", "Exception updating user details", e)
            } finally {
                _isUpdating.value = false
            }
        }
    }
}
