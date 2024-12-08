package com.cst3115.enterprise.taskmanager



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.cst3115.enterprise.taskmanager.ui.screens.CreateAccountScreen
import com.cst3115.enterprise.taskmanager.ui.screens.EditUserDetailsScreen
import com.cst3115.enterprise.taskmanager.MainScreen
import com.cst3115.enterprise.taskmanager.ui.theme.TaskManagerTheme
import com.cst3115.enterprise.taskmanager.ui.viewmodel.TaskDetailViewModel
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
        composable("task_details/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toInt() ?: 0
            val taskDetailViewModel: TaskDetailViewModel = viewModel()
            LaunchedEffect(taskId) {
                taskDetailViewModel.loadTaskDetails(taskId)
            }
            TaskDetailScreen(navController, taskDetailViewModel)
        }
        composable("create_task") {
            CreateTaskScreen(navController)
        }
    }
}