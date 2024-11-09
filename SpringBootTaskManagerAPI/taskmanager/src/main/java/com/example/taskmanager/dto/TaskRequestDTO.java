package com.example.taskmanager.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class TaskRequestDTO {
    @NotBlank(message = "Task name is required")
    @Size(max = 45, message = "Task name must be at most 45 characters")
    private String taskName;

    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;

    @NotNull(message = "Deadline is required")
    private LocalDateTime deadline;

    private Integer dependencyTaskId; // Optional

    @NotNull(message = "Created By ID is required")
    private Integer createdById;

    private Integer priorityId; // Optional

    private Integer statusId; // Add this field if required

    private Integer assignedEmployeeId; // Add this field if required
}
