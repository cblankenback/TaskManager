package com.cst3115.enterprise.taskmanager.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cst3115.enterprise.taskmanager.model.Priority
import com.cst3115.enterprise.taskmanager.model.Task
import com.cst3115.enterprise.taskmanager.network.CreateTaskRequest
import com.cst3115.enterprise.taskmanager.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskCreateViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = RetrofitInstance.getApiService(application.applicationContext)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _priorities = MutableStateFlow<List<Priority>>(emptyList())
    val priorities: StateFlow<List<Priority>> = _priorities

    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())
    val allTasks: StateFlow<List<Task>> = _allTasks

    fun loadDataForCreation() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val prioritiesResponse = apiService.getPriorities()
                if (prioritiesResponse.isSuccessful) {
                    _priorities.value = prioritiesResponse.body() ?: emptyList()
                } else {
                    Log.e("TaskCreateViewModel", "Failed to load priorities: ${prioritiesResponse.message()}")
                }

                val tasksResponse = apiService.getTasks()
                if (tasksResponse.isSuccessful) {
                    _allTasks.value = tasksResponse.body() ?: emptyList()
                } else {
                    Log.e("TaskCreateViewModel", "Failed to load tasks: ${tasksResponse.message()}")
                }

            } catch (e: Exception) {
                Log.e("TaskCreateViewModel", "Exception loading data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createTask(
        taskName: String,
        description: String,
        deadline: String,
        address: String,
        createdById: Int,
        priorityId: Int,
        dependencyTaskId: Int?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _success.value = false

            val request = CreateTaskRequest(
                taskName = taskName,
                description = description,
                deadline = deadline,
                address = address,
                createdById = createdById,
                priorityId = priorityId,
                dependencyTaskId = dependencyTaskId
            )

            try {
                val response = apiService.createTask(request)
                if (response.isSuccessful) {
                    _success.value = true
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error occurred."
                    _errorMessage.value = "Failed to create task: $error"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
                Log.e("TaskCreateViewModel", "Exception during task creation", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
