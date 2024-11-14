package com.cst3115.enterprise.taskmanager.ui.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.cst3115.enterprise.taskmanager.model.LoginRequestDTO
import com.cst3115.enterprise.taskmanager.model.LoginResponseDTO
import com.cst3115.enterprise.taskmanager.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for handling user login.
 *
 * @param application The application context.
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    // Private mutable state flows
    private val _isLoading = MutableStateFlow(false)
    private val _loginError = MutableStateFlow<String?>(null)

    // Public immutable state flows
    val isLoading: StateFlow<Boolean> = _isLoading
    val loginError: StateFlow<String?> = _loginError

    // Context from Application
    private val context: Context = getApplication<Application>().applicationContext

    // MasterKey for encryption
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // EncryptedSharedPreferences for secure token storage
    private val securePrefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs", // File name
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * Performs user login by interacting with the backend API.
     *
     * @param userId The user's ID.
     * @param password The user's password.
     * @param onSuccess Callback invoked upon successful login.
     */
    fun login(userId: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginError.value = null

            try {
                // Create login request DTO
                val loginRequest = LoginRequestDTO(
                    userId = userId,
                    password = password
                )

                // Make API call
                val apiService = RetrofitInstance.getApiService(context)
                val response = apiService.loginUser(loginRequest)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("LoginViewModel", "Login successful: $loginResponse")

                    // Store the token securely
                    loginResponse?.token?.let { token ->
                        securePrefs.edit().putString("auth_token", token).apply()
                        Log.d("LoginViewModel", "Token stored securely.")
                    }

                    // Proceed with success action
                    onSuccess()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Login failed."
                    _loginError.value = errorMsg
                    Log.e("LoginViewModel", "Login failed: $errorMsg")
                }
            } catch (e: Exception) {
                _loginError.value = "An error occurred: ${e.localizedMessage}"
                Log.e("LoginViewModel", "Error during login", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Sets a login error message.
     *
     * @param message The error message to display.
     */
    fun setLoginError(message: String) {
        _loginError.value = message
    }

    /**
     * Clears any existing login error messages.
     */
    fun clearLoginError() {
        _loginError.value = null
    }

    /**
     * Retrieves the stored authentication token.
     *
     * @return The authentication token if available, null otherwise.
     */
    fun getAuthToken(): String? {
        return securePrefs.getString("auth_token", null)
    }

    /**
     * Clears the stored authentication token (e.g., during logout).
     */
    fun clearAuthToken() {
        securePrefs.edit().remove("auth_token").apply()
        Log.d("LoginViewModel", "Authentication token cleared.")
    }
}
