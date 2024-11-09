package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeTaskRequestDTO {

    @NotNull(message = "Task ID is required")
    private Integer taskId;

    @NotNull(message = "Employee ID is required")
    private Integer employeeId;

    @NotNull(message = "Assigned date is required")
    @PastOrPresent(message = "Assigned date cannot be in the future")
    private LocalDateTime assignedDate;
}
