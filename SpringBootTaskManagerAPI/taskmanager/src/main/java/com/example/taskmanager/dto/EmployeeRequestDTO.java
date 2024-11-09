package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeRequestDTO {

    @NotNull(message = "First name is required")
    @Size(max = 45, message = "First name must be at most 45 characters")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Size(max = 45, message = "Last name must be at most 45 characters")
    private String lastName;

    // Password is required during creation but optional during update
    private String password;

    @NotNull(message = "Department ID is required")
    private Integer departmentId;

    @NotNull(message = "Availability ID is required")
    private Integer availabilityId;

    @NotNull(message = "Role ID is required")
    private Integer roleId;
}
