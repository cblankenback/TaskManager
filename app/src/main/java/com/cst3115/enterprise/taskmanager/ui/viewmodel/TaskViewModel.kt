package com.cst3115.enterprise.taskmanager.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cst3115.enterprise.taskmanager.model.Task
import com.cst3115.enterprise.taskmanager.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val apiService = RetrofitInstance.getApiService(context)

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = apiService.getTasks()
                if (response.isSuccessful) {
                    _tasks.value = response.body() ?: emptyList()
                    Log.d("TaskViewModel", "Tasks loaded: ${_tasks.value}")
                } else {
                    val msg = response.errorBody()?.string() ?: "Failed to load tasks."
                    _errorMessage.value = msg
                    Log.e("TaskViewModel", msg)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading tasks: ${e.localizedMessage}"
                Log.e("TaskViewModel", "Error loading tasks", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
