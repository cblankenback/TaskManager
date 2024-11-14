// File: com/cst3115/enterprise/taskmanager/ui/screens/EditUserDetailsScreen.kt

package com.cst3115.enterprise.taskmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cst3115.enterprise.taskmanager.model.Availability
import com.cst3115.enterprise.taskmanager.model.Department
import com.cst3115.enterprise.taskmanager.model.Role
import com.cst3115.enterprise.taskmanager.ui.components.ExposedDropdownMenuComponent
import com.cst3115.enterprise.taskmanager.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch

/**
 * Composable function representing the Edit User Details Screen.
 *
 * @param navController The NavController used for navigating between screens.
 * @param userViewModel The UserViewModel instance for handling data operations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserDetailsScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    // Collect user details from ViewModel
    val userDetails by userViewModel.userDetails.collectAsState()
    val isUpdating by userViewModel.isUpdating.collectAsState()
    val updateErrorMessage by userViewModel.updateErrorMessage.collectAsState()

    // Collect necessary lists for dropdowns from ViewModel
    val departments by userViewModel.departments.collectAsState()
    val isLoadingDepartments by userViewModel.isLoadingDepartments.collectAsState()
    val availabilities by userViewModel.availabilities.collectAsState()
    val isLoadingAvailabilities by userViewModel.isLoadingAvailabilities.collectAsState()
    val roles by userViewModel.roles.collectAsState()
    val isLoadingRoles by userViewModel.isLoadingRoles.collectAsState()

    // Editable fields initialized with current user details
    var firstName by remember { mutableStateOf(userDetails?.firstName ?: "") }
    var lastName by remember { mutableStateOf(userDetails?.lastName ?: "") }
    var selectedDepartment by remember { mutableStateOf<Department?>(null) }
    var selectedAvailability by remember { mutableStateOf<Availability?>(null) }
    var selectedRole by remember { mutableStateOf<Role?>(null) }

    // Initialize selected items with current user details
    LaunchedEffect(userDetails, departments, availabilities, roles) {
        userDetails?.let { details ->
            firstName = details.firstName
            lastName = details.lastName
            selectedDepartment = departments.find { it.departmentId == details.departmentId }
            selectedAvailability = availabilities.find { it.availabilityId == details.availabilityId }
            selectedRole = roles.find { it.roleId == details.roleId }
        }
    }

    // Handle Snackbar for error messages
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(updateErrorMessage) {
        updateErrorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Handle input validation errors
    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var departmentError by remember { mutableStateOf<String?>(null) }
    var availabilityError by remember { mutableStateOf<String?>(null) }
    var roleError by remember { mutableStateOf<String?>(null) }

    // Coroutine Scope for Snackbar
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Edit User Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // Perform input validation
                            firstNameError = if (firstName.isBlank()) "First name cannot be empty" else null
                            lastNameError = if (lastName.isBlank()) "Last name cannot be empty" else null
                            departmentError = if (selectedDepartment == null) "Please select a department" else null
                            availabilityError = if (selectedAvailability == null) "Please select an availability" else null
                            roleError = if (selectedRole == null) "Please select a role" else null

                            // Check if any errors exist
                            if (firstNameError == null && lastNameError == null &&
                                departmentError == null && availabilityError == null &&
                                roleError == null
                            ) {
                                // Proceed with update
                                userViewModel.updateUserDetails(
                                    firstName = firstName,
                                    lastName = lastName,
                                    departmentId = selectedDepartment!!.departmentId,
                                    availabilityId = selectedAvailability!!.availabilityId,
                                    roleId = selectedRole!!.roleId,
                                    onSuccess = {
                                        // Show success message and navigate back
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Details updated successfully.")
                                        }
                                        navController.popBackStack()
                                    },
                                    onError = { /* Handled via Snackbar */ }
                                )
                            } else {
                                // Show validation errors via Snackbar
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Please fix the errors before saving.")
                                }
                            }
                        },
                        enabled = !isUpdating
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Save"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            // Scrollable Column for the form
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // First Name Field
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    isError = firstNameError != null,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = {
                        if (firstNameError != null) {
                            Text(firstNameError!!, color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Last Name Field
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    isError = lastNameError != null,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = {
                        if (lastNameError != null) {
                            Text(lastNameError!!, color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Department Dropdown
                ExposedDropdownMenuComponent(
                    label = "Department",
                    selectedItem = selectedDepartment,
                    onItemSelected = { selectedDepartment = it },
                    items = departments,
                    isLoading = isLoadingDepartments,
                    itemLabel = { it.departmentName }
                )
                if (departmentError != null) {
                    Text(
                        text = departmentError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Availability Dropdown
                ExposedDropdownMenuComponent(
                    label = "Availability",
                    selectedItem = selectedAvailability,
                    onItemSelected = { selectedAvailability = it },
                    items = availabilities,
                    isLoading = isLoadingAvailabilities,
                    itemLabel = { it.availabilityName }
                )
                if (availabilityError != null) {
                    Text(
                        text = availabilityError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Role Dropdown
                ExposedDropdownMenuComponent(
                    label = "Role",
                    selectedItem = selectedRole,
                    onItemSelected = { selectedRole = it },
                    items = roles,
                    isLoading = isLoadingRoles,
                    itemLabel = { it.roleName }
                )
                if (roleError != null) {
                    Text(
                        text = roleError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Loading Indicator during Update
                if (isUpdating) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        })
}

