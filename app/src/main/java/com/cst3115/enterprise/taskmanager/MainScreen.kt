package com.cst3115.enterprise.taskmanager.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.cst3115.enterprise.taskmanager.ui.viewmodel.UserViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.rememberCoroutineScope
import com.cst3115.enterprise.taskmanager.util.TokenProvider
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, userViewModel: UserViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Manager") },
                actions = {
                    // User Profile Icon
                    IconButton(onClick = { navController.navigate("user_details") }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "User Profile"
                        )
                    }
                    // Logout Icon
                    IconButton(onClick = {
                        coroutineScope.launch {
                            // Clear the JWT token
                            TokenProvider.clearToken(context)
                            // Navigate to login screen and clear the back stack
                            navController.navigate("login") {
                                popUpTo("main") { inclusive = true }
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AppContent()
            }
        }
    )
}

@Composable
fun AppContent() {
    // Replace with your actual main screen content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome to the Main Screen!", style = MaterialTheme.typography.headlineMedium)
    }
}
