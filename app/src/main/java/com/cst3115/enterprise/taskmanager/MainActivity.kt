package com.cst3115.enterprise.taskmanager



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.cst3115.enterprise.taskmanager.ui.screens.CreateAccountScreen
import com.cst3115.enterprise.taskmanager.ui.screens.EditUserDetailsScreen
import com.cst3115.enterprise.taskmanager.ui.screens.MainScreen
import com.cst3115.enterprise.taskmanager.ui.theme.TaskManagerTheme
import com.cst3115.enterprise.taskmanager.ui.viewmodel.UserViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagerTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("main") {
            val userViewModel: UserViewModel = viewModel()
            MainScreen(navController, userViewModel)
        }
        composable("create_account") {
            CreateAccountScreen(navController)
        }
        composable("user_details") {
            // Pass the navController and UserViewModel to UserDetailsScreen
            val userViewModel: UserViewModel = viewModel()
            UserDetailsScreen(navController, userViewModel)
        }
        composable("edit_user_details") {
            val userViewModel: UserViewModel = viewModel()
            EditUserDetailsScreen(navController, userViewModel)
        }
    }
}