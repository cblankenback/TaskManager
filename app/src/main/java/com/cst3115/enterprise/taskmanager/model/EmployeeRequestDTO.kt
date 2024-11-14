package com.cst3115.enterprise.taskmanager.model

data class EmployeeRequestDTO(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val departmentId: Int,
    val availabilityId: Int,
    val roleId: Int
)
