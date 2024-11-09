package com.example.taskmanager.dto;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskResponseDTO {
    private Integer taskId;
    private String taskName;
    private String description;
    private LocalDateTime deadline;
    private LocalDateTime creationDate;
    private Boolean archived;
    private Integer dependencyTaskId; // Optional
    private Integer createdById;
    private Integer priorityId; // Optional
}

