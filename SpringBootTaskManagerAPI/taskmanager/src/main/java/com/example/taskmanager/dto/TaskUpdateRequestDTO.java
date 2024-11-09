package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskUpdateRequestDTO {

    @NotNull(message = "Task ID is required")
    private Integer taskId;

    private Integer commentId; // Optional

    @NotNull(message = "Status ID is required")
    private Integer statusId;

    @NotNull(message = "Employee ID is required")
    private Integer employeeId;

    // updateDate is set by the system
}
