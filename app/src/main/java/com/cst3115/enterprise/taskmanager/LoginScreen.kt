package com.cst3115.enterprise.taskmanager

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cst3115.enterprise.taskmanager.ui.viewmodel.LoginViewModel
import com.cst3115.enterprise.taskmanager.ui.theme.TaskManagerTheme
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    val context = LocalContext.current

    // Load saved username from SharedPreferences
    val savedUsername = remember {
        val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        prefs.getString("saved_username", "") ?: ""
    }

    // Form Fields
    var userId by remember { mutableStateOf(savedUsername) }
    var password by remember { mutableStateOf("") }

    // UI State
    val isLoading = viewModel.isLoading.collectAsState().value
    val loginError = viewModel.loginError.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // User ID Field (pre-filled with savedUsername if available)
        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("User ID") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )

        if (loginError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = loginError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = {
                // Input Validation
                if (userId.isBlank() || password.isBlank()) {
                    viewModel.setLoginError("Please enter both User ID and Password.")
                    return@Button
                }

                // Perform Login
                viewModel.login(userId, password) {
                    // On success, save username to SharedPreferences
                    val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                    prefs.edit().putString("saved_username", userId).apply()

                    // Navigate to Main Screen upon successful login
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logging in...")
            } else {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Create Account TextButton
        TextButton(
            onClick = {
                navController.navigate("create_account")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    TaskManagerTheme {
        LoginScreen(navController)
    }
}
