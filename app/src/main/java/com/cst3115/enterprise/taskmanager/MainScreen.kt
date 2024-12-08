package com.cst3115.enterprise.taskmanager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import com.cst3115.enterprise.taskmanager.ui.viewmodel.FavoriteTaskViewModel
import com.cst3115.enterprise.taskmanager.model.Task
import com.cst3115.enterprise.taskmanager.model.FavoriteTask

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, userViewModel: UserViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val taskViewModel: TaskViewModel = viewModel()
    val favoriteTaskViewModel: FavoriteTaskViewModel = viewModel()

    // Load tasks when MainScreen is composed
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

            val favoriteTasks by favoriteTaskViewModel.allFavoriteTasks.collectAsState()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    errorMessage != null -> {
                        Text(
                            text = errorMessage ?: "Unknown Error",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text("All Tasks", style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            TasksList(tasks, navController, favoriteTaskViewModel)

                            Spacer(modifier = Modifier.height(24.dp))

                            Text("Favorite Tasks", style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            FavoriteTasksList(favoriteTasks, navController, favoriteTaskViewModel)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun TasksList(
    tasks: List<Task>,
    navController: NavController,
    favoriteTaskViewModel: FavoriteTaskViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(tasks) { task ->
            TaskItem(task, navController, favoriteTaskViewModel)
        }
    }
}

@Composable
fun FavoriteTasksList(
    favoriteTasks: List<FavoriteTask>,
    navController: NavController,
    favoriteTaskViewModel: FavoriteTaskViewModel
) {
    if (favoriteTasks.isEmpty()) {
        Text("No favorite tasks yet.", style = MaterialTheme.typography.bodyMedium)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(favoriteTasks) { favoriteTask ->
                FavoriteTaskItem(favoriteTask, navController, favoriteTaskViewModel)
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    navController: NavController,
    favoriteTaskViewModel: FavoriteTaskViewModel
) {
    val isFavorited by favoriteTaskViewModel.isTaskFavorited(task.taskId).collectAsState(initial = false)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("task_details/${task.taskId}") },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Task ID: ${task.taskId}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Task Name: ${task.taskName}", style = MaterialTheme.typography.bodyLarge)
            }
            IconButton(onClick = {
                if (isFavorited) {
                    favoriteTaskViewModel.removeFavoriteTask(FavoriteTask(task.taskId, task.taskName))
                } else {
                    favoriteTaskViewModel.addFavoriteTask(task.taskId, task.taskName)
                }
            }) {
                Icon(
                    imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorited) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorited) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun FavoriteTaskItem(
    favoriteTask: FavoriteTask,
    navController: NavController,
    favoriteTaskViewModel: FavoriteTaskViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("task_details/${favoriteTask.taskId}") },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Task ID: ${favoriteTask.taskId}", style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = {
                favoriteTaskViewModel.removeFavoriteTask(favoriteTask)
            }) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Remove from favorites",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
