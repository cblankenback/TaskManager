package com.cst3115.enterprise.taskmanager.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cst3115.enterprise.taskmanager.model.LoginRequestDTO
import com.cst3115.enterprise.taskmanager.model.LoginResponseDTO
import com.cst3115.enterprise.taskmanager.network.RetrofitInstance
import com.cst3115.enterprise.taskmanager.util.TokenProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * ViewModel for handling user authentication (login).
 *
 * @param application The application context.
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val apiService = RetrofitInstance.getApiService(context)

    // StateFlow for Login Response
    private val _loginResponse = MutableStateFlow<LoginResponseDTO?>(null)
    val loginResponse: StateFlow<LoginResponseDTO?> = _loginResponse

    // StateFlow for Loading State
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // StateFlow for Error Messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /**
     * Logs in the user by sending credentials to the backend.
     *
     * @param loginRequest The login request containing username and password.
     */
    fun loginUser(loginRequest: LoginRequestDTO) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response: Response<LoginResponseDTO> = apiService.loginUser(loginRequest)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _loginResponse.value = responseBody
                        // Save the token securely
                        TokenProvider.saveToken(context, responseBody.token)
                        Log.d("AuthViewModel", "Login successful: $responseBody")
                    } else {
                        _errorMessage.value = "Login failed: Empty response."
                        Log.e("AuthViewModel", "Login failed: Empty response.")
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Login failed."
                    _errorMessage.value = errorMsg
                    Log.e("AuthViewModel", "Login failed: $errorMsg")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error during login: ${e.localizedMessage}"
                Log.e("AuthViewModel", "Exception during login", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Logs out the user by clearing the stored token.
     */
    fun logoutUser() {
        TokenProvider.clearToken(context)
        _loginResponse.value = null
        Log.d("AuthViewModel", "User logged out successfully.")
    }
}
