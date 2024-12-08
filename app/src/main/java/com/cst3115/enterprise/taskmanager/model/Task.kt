package com.cst3115.enterprise.taskmanager.model


import com.google.gson.annotations.SerializedName

data class Task(
    val taskId: Int,
    val taskName: String,
    val description: String,
    val deadline: String,
    val creationDate: String,
    val archived: Boolean,
    val dependencyTaskId: Int?,
    val createdById: Int,
    val priorityId: Int,
    val address: String
)


