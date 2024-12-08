package com.cst3115.enterprise.taskmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tasks")
data class FavoriteTask(
    @PrimaryKey val taskId: Int,
    val taskName: String
)
