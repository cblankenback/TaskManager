package com.cst3115.enterprise.taskmanager


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cst3115.enterprise.taskmanager.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(navController: NavController, userViewModel: UserViewModel) {
    val userDetails by userViewModel.userDetails.collectAsState()
    val isLoading by userViewModel.isLoading.collectAsState()
    val errorMessage by userViewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Details") },
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
                            // Navigate to EditUserDetailsScreen
                            navController.navigate("edit_user_details")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Details"
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
                    userDetails != null -> {
                        UserDetailsView(userDetails = userDetails!!)
                    }
                }
            }
        }
    )
}

@Composable
fun UserDetailsView(userDetails: com.cst3115.enterprise.taskmanager.model.UserDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Employee ID: ${userDetails.employeeId}", style = MaterialTheme.typography.titleMedium)
        Text(text = "Username: ${userDetails.username}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "First Name: ${userDetails.firstName}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Last Name: ${userDetails.lastName}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Department ID: ${userDetails.departmentId}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Availability ID: ${userDetails.availabilityId}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Role ID: ${userDetails.roleId}", style = MaterialTheme.typography.bodyLarge)
    }
}
