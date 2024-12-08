package com.cst3115.enterprise.taskmanager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cst3115.enterprise.taskmanager.ui.viewmodel.UserViewModel
import com.cst3115.enterprise.taskmanager.util.TokenProvider
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cst3115.enterprise.taskmanager.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, userViewModel: UserViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val taskViewModel: TaskViewModel = viewModel()

    LaunchedEffect(Unit) {
        taskViewModel.loadTasks()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Manager") },
                actions = {
                    IconButton(onClick = { navController.navigate("user_details") }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "User Profile"
                        )
                    }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            TokenProvider.clearToken(context)
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
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("create_task") }) {
                Text("+")
            }
        },
        content = { paddingValues ->
            val tasks by taskViewModel.tasks.collectAsState()
            val isLoading by taskViewModel.isLoading.collectAsState()
            val errorMessage by taskViewModel.errorMessage.collectAsState()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                when {
                    isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    errorMessage != null -> Text(text = errorMessage ?: "Unknown Error", color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                    else -> TasksList(tasks, navController)
                }
            }
        }
    )
}

@Composable
fun TasksList(tasks: List<com.cst3115.enterprise.taskmanager.model.Task>, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(tasks) { task ->
            TaskItem(task) { taskId ->
                navController.navigate("task_details/$taskId")
            }
        }
    }
}

@Composable
fun TaskItem(task: com.cst3115.enterprise.taskmanager.model.Task, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(task.taskId) },
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Task ID: ${task.taskId}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Task Name: ${task.taskName}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
