package com.example.taskmanager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeNotificationResponseDTO {
    private Integer employeeNotificationId;
    private Boolean isRead;
    private LocalDateTime received;
    private Integer notificationId;
    private Integer employeeId;
}
