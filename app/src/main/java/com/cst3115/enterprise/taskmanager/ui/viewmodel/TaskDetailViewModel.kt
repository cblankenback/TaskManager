package com.cst3115.enterprise.taskmanager.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cst3115.enterprise.taskmanager.model.*
import com.cst3115.enterprise.taskmanager.network.CreateTaskUpdateRequest
import com.cst3115.enterprise.taskmanager.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TaskDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val apiService = RetrofitInstance.getApiService(context)

    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> = _task

    private val _updates = MutableStateFlow<List<TaskUpdate>>(emptyList())
    val updates: StateFlow<List<TaskUpdate>> = _updates

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Statuses
    private val _statuses = MutableStateFlow<List<Status>>(emptyList())
    val statuses: StateFlow<List<Status>> = _statuses

    // Caches for employees and priorities to avoid multiple network calls
    private val employeeCache = mutableMapOf<Int, EmployeeResponseDTO>()
    private val priorityCache = mutableMapOf<Int, Priority>()

    fun loadTaskDetails(taskId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // Fetch Task
                val taskResponse = apiService.getTask(taskId)
                if (taskResponse.isSuccessful) {
                    val fetchedTask = taskResponse.body()
                    _task.value = fetchedTask

                    // Pre-fetch the employee who created the task
                    fetchedTask?.createdById?.let { creatorId ->
                        fetchEmployeeIfNeeded(creatorId)
                    }

                    // Pre-fetch the priority
                    fetchedTask?.priorityId?.let { priorityId ->
                        fetchPriorityIfNeeded(priorityId)
                    }

                } else {
                    _errorMessage.value = "Failed to load task details: ${taskResponse.message()}"
                    Log.e("TaskDetailViewModel", "Task fetch failed: ${taskResponse.message()}")
                }

                // Fetch Updates
                val updatesResponse = apiService.getTaskUpdates(taskId)
                if (updatesResponse.isSuccessful) {
                    val rawUpdates = updatesResponse.body() ?: emptyList()
                    // Pre-fetch employees and we have statuses already
                    for (update in rawUpdates) {
                        fetchEmployeeIfNeeded(update.employeeId)
                    }

                    // Sort updates
                    val sortedUpdates = rawUpdates.sortedByDescending { parseDate(it.updateDate) }
                    _updates.value = sortedUpdates
                } else {
                    _errorMessage.value = "Failed to load task updates: ${updatesResponse.message()}"
                    Log.e("TaskDetailViewModel", "Task updates fetch failed: ${updatesResponse.message()}")
                }

            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
                Log.e("TaskDetailViewModel", "Exception loading task details", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadStatuses() {
        viewModelScope.launch {
            try {
                val response = apiService.getStatuses()
                if (response.isSuccessful) {
                    _statuses.value = response.body() ?: emptyList()
                } else {
                    Log.e("TaskDetailViewModel", "Failed to load statuses: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("TaskDetailViewModel", "Exception fetching statuses", e)
            }
        }
    }

    fun createTaskUpdate(
        taskId: Int,
        comment: String,
        statusId: Int,
        employeeId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val request = CreateTaskUpdateRequest(taskId, comment, statusId, employeeId)
                val response = apiService.createTaskUpdate(request)
                if (response.isSuccessful) {
                    // Refresh the updates after successful creation
                    loadTaskDetails(taskId)
                    onSuccess()
                } else {
                    val msg = response.errorBody()?.string() ?: "Failed to create task update."
                    onError(msg)
                    Log.e("TaskDetailViewModel", "Create update failed: $msg")
                }
            } catch (e: Exception) {
                val msg = "Error creating task update: ${e.localizedMessage}"
                onError(msg)
                Log.e("TaskDetailViewModel", msg, e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun parseDate(dateStr: String): ZonedDateTime {
        val localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        return localDateTime.atZone(ZoneId.systemDefault())
    }

    private suspend fun fetchEmployeeIfNeeded(employeeId: Int) {
        if (!employeeCache.containsKey(employeeId)) {
            val response = apiService.getEmployee(employeeId)
            if (response.isSuccessful) {
                response.body()?.let {
                    employeeCache[employeeId] = it
                }
            } else {
                Log.e("TaskDetailViewModel", "Failed to fetch employee $employeeId: ${response.message()}")
            }
        }
    }

    private suspend fun fetchPriorityIfNeeded(priorityId: Int) {
        if (!priorityCache.containsKey(priorityId)) {
            val response = apiService.getPriority(priorityId)
            if (response.isSuccessful) {
                response.body()?.let {
                    priorityCache[priorityId] = it
                }
            } else {
                Log.e("TaskDetailViewModel", "Failed to fetch priority $priorityId: ${response.message()}")
            }
        }
    }

    fun getEmployeeName(employeeId: Int): String {
        val emp = employeeCache[employeeId]
        return if (emp != null) "${emp.firstName} ${emp.lastName}" else "Employee #$employeeId"
    }

    fun getStatusName(statusId: Int): String {
        val st = statuses.value.find { it.statusId == statusId }
        return st?.statusName ?: "Status #$statusId"
    }

    fun getPriorityType(priorityId: Int): String {
        val pr = priorityCache[priorityId]
        return pr?.type ?: "Priority #$priorityId"
    }
}
