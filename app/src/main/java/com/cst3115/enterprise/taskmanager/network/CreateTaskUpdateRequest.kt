package com.cst3115.enterprise.taskmanager.network


data class CreateTaskUpdateRequest(
    val taskId: Int,
    val comment: String,
    val statusId: Int,
    val employeeId: Int
)
