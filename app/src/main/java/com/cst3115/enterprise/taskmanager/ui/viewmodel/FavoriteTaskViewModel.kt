// File: com/cst3115/enterprise/taskmanager/ui/viewmodel/FavoriteTaskViewModel.kt
package com.cst3115.enterprise.taskmanager.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cst3115.enterprise.taskmanager.AppDatabase
import com.cst3115.enterprise.taskmanager.FavoriteTaskRepository
import com.cst3115.enterprise.taskmanager.model.FavoriteTask
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoriteTaskViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).favoriteTaskDao()
    private val repository = FavoriteTaskRepository(dao)

    val allFavoriteTasks: StateFlow<List<FavoriteTask>> = repository.allFavoriteTasks
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addFavoriteTask(taskId: Int, taskName: String) = viewModelScope.launch {
        repository.addFavoriteTask(FavoriteTask(taskId, taskName))
    }

    fun removeFavoriteTask(task: FavoriteTask) = viewModelScope.launch {
        repository.removeFavoriteTask(task)
    }

    fun isTaskFavorited(taskId: Int): StateFlow<Boolean> {
        return repository.isTaskFavorited(taskId)
            .stateIn(viewModelScope, SharingStarted.Lazily, false)
    }
}
