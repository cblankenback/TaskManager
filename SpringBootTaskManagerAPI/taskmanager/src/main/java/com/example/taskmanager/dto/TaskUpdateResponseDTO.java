package com.example.taskmanager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskUpdateResponseDTO {
    private Integer taskUpdateId;
    private LocalDateTime updateDate;
    private Integer taskId;
    private String comment;
    private Integer statusId;
    private Integer employeeId;
}
