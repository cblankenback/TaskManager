package com.cst3115.enterprise.taskmanager

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cst3115.enterprise.taskmanager.model.Priority
import com.cst3115.enterprise.taskmanager.model.Task
import com.cst3115.enterprise.taskmanager.ui.viewmodel.TaskCreateViewModel
import com.cst3115.enterprise.taskmanager.ui.viewmodel.UserViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    taskCreateViewModel: TaskCreateViewModel = viewModel()
) {
    val userDetails by userViewModel.userDetails.collectAsState()
    val employeeId = userDetails?.employeeId

    // Form fields
    var taskName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    // Priority dropdown
    val priorities by taskCreateViewModel.priorities.collectAsState()
    var expandedPriority by remember { mutableStateOf(false) }
    var selectedPriority by remember { mutableStateOf<Priority?>(null) }

    // Dependency task dropdown
    val allTasks by taskCreateViewModel.allTasks.collectAsState()
    var expandedDependency by remember { mutableStateOf(false) }
    var selectedDependencyTask by remember { mutableStateOf<Task?>(null) }

    // Date/Time selection states
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    // Time picker state
    val timePickerState = rememberTimePickerState(
        initialHour = 12,
        initialMinute = 0,
        is24Hour = true
    )

    var selectedDateTime by remember { mutableStateOf<LocalDateTime?>(null) }

    // Once we have date/time, we format deadline
    val deadlineText = remember(selectedDateTime) {
        selectedDateTime?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) ?: ""
    }

    val isLoading by taskCreateViewModel.isLoading.collectAsState()
    val errorMessage by taskCreateViewModel.errorMessage.collectAsState()
    val success by taskCreateViewModel.success.collectAsState()

    LaunchedEffect(Unit) {
        taskCreateViewModel.loadDataForCreation()
    }

    LaunchedEffect(success) {
        if (success) {
            navController.popBackStack()
        }
    }

    val isFormValid = employeeId != null &&
            taskName.isNotBlank() &&
            description.isNotBlank() &&
            deadlineText.isNotBlank() &&
            address.isNotBlank() &&
            selectedPriority != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Task") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text("Task Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Deadline field (to show selected or pick)
                OutlinedTextField(
                    value = if (deadlineText.isNotEmpty()) deadlineText else "Select Deadline",
                    onValueChange = {},
                    label = { Text("Deadline") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Pick Deadline")
                        }
                    }
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Priority Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedPriority,
                    onExpandedChange = { expandedPriority = !expandedPriority }
                ) {
                    OutlinedTextField(
                        value = selectedPriority?.type ?: "Select Priority",
                        onValueChange = {},
                        label = { Text("Priority") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPriority) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedPriority,
                        onDismissRequest = { expandedPriority = false }
                    ) {
                        priorities.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.type) },
                                onClick = {
                                    selectedPriority = p
                                    expandedPriority = false
                                }
                            )
                        }
                    }
                }

                // Dependency Task Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedDependency,
                    onExpandedChange = { expandedDependency = !expandedDependency }
                ) {
                    OutlinedTextField(
                        value = selectedDependencyTask?.taskName ?: "Select Dependency (Optional)",
                        onValueChange = {},
                        label = { Text("Dependency Task") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDependency) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDependency,
                        onDismissRequest = { expandedDependency = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("No Dependency") },
                            onClick = {
                                selectedDependencyTask = null
                                expandedDependency = false
                            }
                        )
                        allTasks.forEach { t ->
                            DropdownMenuItem(
                                text = { Text(t.taskName) },
                                onClick = {
                                    selectedDependencyTask = t
                                    expandedDependency = false
                                }
                            )
                        }
                    }
                }

                if (errorMessage != null) {
                    Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)
                }

                if (isLoading) {
                    CircularProgressIndicator()
                }

                Button(
                    onClick = {
                        taskCreateViewModel.createTask(
                            taskName = taskName,
                            description = description,
                            deadline = deadlineText,
                            address = address,
                            createdById = employeeId!!,
                            priorityId = selectedPriority!!.priorityId,
                            dependencyTaskId = selectedDependencyTask?.taskId
                        )
                    },
                    enabled = !isLoading && isFormValid
                ) {
                    Text("Create Task")
                }
            }
        }
    )

    // DatePickerDialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    if (selectedDateMillis != null) {
                        val instant = Instant.ofEpochMilli(selectedDateMillis)
                        val zoneId = ZoneId.systemDefault()
                        val chosenDate = LocalDateTime.ofInstant(instant, zoneId)
                        // Store chosen date, then show time picker
                        selectedDateTime = chosenDate
                        showDatePicker = false
                        showTimePickerDialog = true
                    } else {
                        // No date selected
                    }
                }) {
                    Text("Next")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // TimePicker inside an AlertDialog
    if (showTimePickerDialog) {
        AlertDialog(
            onDismissRequest = { showTimePickerDialog = false },
            title = { Text("Select Time") },
            text = {
                // Use dial time picker
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(onClick = {
                    // On confirm, update selectedDateTime with chosen time
                    val chosenHour = timePickerState.hour
                    val chosenMinute = timePickerState.minute

                    if (selectedDateTime != null) {
                        selectedDateTime = selectedDateTime!!.withHour(chosenHour).withMinute(chosenMinute)
                    }
                    showTimePickerDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePickerDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
