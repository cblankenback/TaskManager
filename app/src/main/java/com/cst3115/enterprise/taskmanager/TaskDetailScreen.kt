package com.cst3115.enterprise.taskmanager

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.cst3115.enterprise.taskmanager.ui.viewmodel.TaskDetailViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cst3115.enterprise.taskmanager.ui.viewmodel.UserLocationViewModel
import com.cst3115.enterprise.taskmanager.ui.viewmodel.UserViewModel
import com.cst3115.enterprise.taskmanager.model.TaskUpdate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    navController: NavController,
    viewModel: TaskDetailViewModel,
    locationViewModel: UserLocationViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val context = LocalContext.current

    // Load user details and statuses on first composition
    LaunchedEffect(Unit) {
        userViewModel.loadCurrentUser()
        viewModel.loadStatuses()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                locationViewModel.fetchLocation()
            } else {
                // Handle permission denial if needed
            }
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val latitude by locationViewModel.latitude.collectAsState()
    val longitude by locationViewModel.longitude.collectAsState()

    val task by viewModel.task.collectAsState()
    val updates by viewModel.updates.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val statuses by viewModel.statuses.collectAsState()
    val userDetails by userViewModel.userDetails.collectAsState()
    val employeeId = userDetails?.employeeId

    var showCreateUpdateDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(task?.taskName ?: "Task Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (task != null) {
                FloatingActionButton(onClick = { showCreateUpdateDialog = true }) {
                    Text("+")
                }
            }
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    errorMessage != null -> {
                        Text(
                            text = errorMessage ?: "Error",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    task != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Top
                        ) {
                            // Display full Task details
                            Text("Task ID: ${task!!.taskId}", style = MaterialTheme.typography.bodyLarge)
                            Text("Task Name: ${task!!.taskName}", style = MaterialTheme.typography.bodyLarge)
                            Text("Description: ${task!!.description}", style = MaterialTheme.typography.bodyLarge)
                            Text("Deadline: ${task!!.deadline}", style = MaterialTheme.typography.bodyLarge)
                            Text("Creation Date: ${task!!.creationDate}", style = MaterialTheme.typography.bodyLarge)
                            Text("Archived: ${task!!.archived}", style = MaterialTheme.typography.bodyLarge)
                            Text("Dependency Task ID: ${task!!.dependencyTaskId ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                            Text("Created By: ${viewModel.getEmployeeName(task!!.createdById)}", style = MaterialTheme.typography.bodyLarge)
                            Text("Priority: ${viewModel.getPriorityType(task!!.priorityId)}", style = MaterialTheme.typography.bodyLarge)
                            Text("Address: ${task!!.address}", style = MaterialTheme.typography.bodyLarge)

                            Spacer(modifier = Modifier.height(16.dp))

                            // Display user's actual device location
                            if (latitude != null && longitude != null) {
                                Text("Your current Latitude: $latitude", style = MaterialTheme.typography.bodyLarge)
                                Text("Your current Longitude: $longitude", style = MaterialTheme.typography.bodyLarge)
                            } else {
                                Text("Fetching device location...", style = MaterialTheme.typography.bodyLarge)
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text("Updates:", style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(8.dp))

                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(updates) { update ->
                                    TaskUpdateItem(update, viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    )

    if (showCreateUpdateDialog && task != null) {
        CreateUpdateDialog(
            taskId = task!!.taskId,
            statuses = statuses,
            employeeId = employeeId,
            onDismiss = { showCreateUpdateDialog = false },
            onCreateUpdate = { comment, statusId, empId ->
                viewModel.createTaskUpdate(
                    taskId = task!!.taskId,
                    comment = comment,
                    statusId = statusId,
                    employeeId = empId,
                    onSuccess = { showCreateUpdateDialog = false },
                    onError = {
                        // Show a Snackbar or Toast
                        showCreateUpdateDialog = false
                    }
                )
            }
        )
    }
}

@Composable
fun TaskUpdateItem(update: TaskUpdate, viewModel: TaskDetailViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Update Date: ${update.updateDate}", style = MaterialTheme.typography.labelSmall)
            Text("Comment: ${update.comment}", style = MaterialTheme.typography.bodyLarge)
            Text("Status: ${viewModel.getStatusName(update.statusId)}", style = MaterialTheme.typography.bodyMedium)
            Text("Employee: ${viewModel.getEmployeeName(update.employeeId)}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUpdateDialog(
    taskId: Int,
    statuses: List<com.cst3115.enterprise.taskmanager.model.Status>,
    employeeId: Int?,
    onDismiss: () -> Unit,
    onCreateUpdate: (comment: String, statusId: Int, employeeId: Int) -> Unit
) {
    var comment by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf<com.cst3115.enterprise.taskmanager.model.Status?>(null) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Create Task Update") },
        text = {
            Column {
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Comment") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Status Dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedStatus?.statusName ?: "Select Status",
                        onValueChange = {},
                        label = { Text("Status") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        statuses.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.statusName) },
                                onClick = {
                                    selectedStatus = status
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Employee ID: ${employeeId ?: "N/A"}")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val chosenStatusId = selectedStatus?.statusId
                val chosenEmployeeId = employeeId
                if (chosenStatusId != null && chosenEmployeeId != null) {
                    onCreateUpdate(comment, chosenStatusId, chosenEmployeeId)
                } else {
                    // Show error or require user to select status / have employee ID
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}
