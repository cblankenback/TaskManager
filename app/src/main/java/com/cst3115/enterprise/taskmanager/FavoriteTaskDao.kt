package com.cst3115.enterprise.taskmanager

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cst3115.enterprise.taskmanager.model.FavoriteTask // Ensure this is from `model` package
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTaskDao {
    @Query("SELECT * FROM favorite_tasks")
    fun getAllFavoriteTasks(): Flow<List<FavoriteTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteTask(task: FavoriteTask): Long // Now returns Long

    @Delete
    suspend fun removeFavoriteTask(task: FavoriteTask): Int // Now returns Int

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_tasks WHERE taskId = :taskId)")
    fun isTaskFavorited(taskId: Int): Flow<Boolean>
}
