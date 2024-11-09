package com.example.taskmanager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeTaskResponseDTO {
    private Integer employeeTaskId;
    private Integer taskId;
    private Integer employeeId;
    private LocalDateTime assignedDate;
}
