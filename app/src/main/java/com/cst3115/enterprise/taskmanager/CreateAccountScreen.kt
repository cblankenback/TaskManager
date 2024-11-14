package com.cst3115.enterprise.taskmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cst3115.enterprise.taskmanager.model.EmployeeRequestDTO
import com.cst3115.enterprise.taskmanager.ui.components.ExposedDropdownMenuComponent
import com.cst3115.enterprise.taskmanager.ui.viewmodel.CreateAccountViewModel
import com.cst3115.enterprise.taskmanager.ui.theme.TaskManagerTheme

/**
 * Composable function representing the Create Account Screen.
 *
 * @param navController The NavController used for navigating between screens.
 * @param viewModel The CreateAccountViewModel instance for handling data operations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(
    navController: NavController,
    viewModel: CreateAccountViewModel = viewModel()
) {
    // Form Fields State
    var username by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Dropdown Selections State
    var selectedDepartment by remember { mutableStateOf<com.cst3115.enterprise.taskmanager.model.Department?>(null) }
    var selectedAvailability by remember { mutableStateOf<com.cst3115.enterprise.taskmanager.model.Availability?>(null) }
    var selectedRole by remember { mutableStateOf<com.cst3115.enterprise.taskmanager.model.Role?>(null) }

    // UI State from ViewModel
    val isLoadingDepartments by viewModel.isLoadingDepartments.collectAsState()
    val isLoadingAvailabilities by viewModel.isLoadingAvailabilities.collectAsState()
    val isLoadingRoles by viewModel.isLoadingRoles.collectAsState()
    val isRegistering by viewModel.isRegistering.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Scroll State for the Column
    val scrollState = rememberScrollState()

    // UI Layout with Scrollable Column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Username Field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // First Name Field
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Last Name Field
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
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

        Spacer(modifier = Modifier.height(16.dp))

        // Department Dropdown
        ExposedDropdownMenuComponent(
            label = "Department",
            selectedItem = selectedDepartment,
            onItemSelected = { selectedDepartment = it },
            items = viewModel.departments.collectAsState().value,
            isLoading = isLoadingDepartments,
            itemLabel = { it.departmentName }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Availability Dropdown
        ExposedDropdownMenuComponent(
            label = "Availability",
            selectedItem = selectedAvailability,
            onItemSelected = { selectedAvailability = it },
            items = viewModel.availabilities.collectAsState().value,
            isLoading = isLoadingAvailabilities,
            itemLabel = { it.availabilityName }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Role Dropdown
        ExposedDropdownMenuComponent(
            label = "Role",
            selectedItem = selectedRole,
            onItemSelected = { selectedRole = it },
            items = viewModel.roles.collectAsState().value,
            isLoading = isLoadingRoles,
            itemLabel = { it.roleName }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Create Account Button
        Button(
            onClick = {
                // Input Validation
                if (username.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank() ||
                    selectedDepartment == null || selectedAvailability == null || selectedRole == null
                ) {
                    viewModel.setErrorMessage("Please fill in all fields.")
                    return@Button
                }

                // Clear previous error messages
                viewModel.clearErrorMessage()

                // Prepare Registration Data
                val employeeRequest = EmployeeRequestDTO(
                    username = username,
                    password = password,
                    firstName = firstName,
                    lastName = lastName,
                    departmentId = selectedDepartment!!.departmentId,
                    availabilityId = selectedAvailability!!.availabilityId,
                    roleId = selectedRole!!.roleId
                )

                // Perform Registration
                viewModel.registerUser(employeeRequest) {
                    // Navigate to Login Screen upon successful registration
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isRegistering
        ) {
            if (isRegistering) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Creating...")
            } else {
                Text("Create Account")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display Error Message if Any
        errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
    /**
     * Preview function for the CreateAccountScreen.
     */
    @Preview(showBackground = true)
    @Composable
    fun CreateAccountScreenPreview() {
        val navController = rememberNavController()
        TaskManagerTheme {
            CreateAccountScreen(navController)
        }
    }

