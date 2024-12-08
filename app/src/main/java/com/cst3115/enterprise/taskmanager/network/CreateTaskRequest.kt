package com.cst3115.enterprise.taskmanager.network

data class CreateTaskRequest(
    val taskName: String,
    val description: String,
    val deadline: String, // Example: "2023-11-30T17:00:00"
    val address: String,
    val createdById: Int,
    val priorityId: Int,
    val dependencyTaskId: Int?
)
