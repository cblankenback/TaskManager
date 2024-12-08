// File: com/cst3115/enterprise/taskmanager/FavoriteTaskRepository.kt
package com.cst3115.enterprise.taskmanager

import com.cst3115.enterprise.taskmanager.model.FavoriteTask
import kotlinx.coroutines.flow.Flow

class FavoriteTaskRepository(private val dao: FavoriteTaskDao) {
    val allFavoriteTasks: Flow<List<FavoriteTask>> = dao.getAllFavoriteTasks()

    suspend fun addFavoriteTask(task: FavoriteTask) = dao.addFavoriteTask(task)
    suspend fun removeFavoriteTask(task: FavoriteTask) = dao.removeFavoriteTask(task)
    fun isTaskFavorited(taskId: Int): Flow<Boolean> = dao.isTaskFavorited(taskId)
}
