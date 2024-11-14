package com.cst3115.enterprise.taskmanager.model

/**
 * Data Transfer Object for Employee responses.
 *
 * @property employeeId Unique identifier for the employee.
 * @property username The username used for login.
 * @property firstName Employee's first name.
 * @property lastName Employee's last name.
 * @property departmentId Identifier for the employee's department.
 * @property availabilityId Identifier for the employee's availability status.
 * @property roleId Identifier for the employee's role.
 */
data class EmployeeResponseDTO(
    val employeeId: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val departmentId: Int,
    val availabilityId: Int,
    val roleId: Int
)
