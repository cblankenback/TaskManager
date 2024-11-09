package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeNotificationRequestDTO {
    
    @NotNull(message = "Notification ID is required")
    private Integer notificationId;
    
    @NotNull(message = "Employee ID is required")
    private Integer employeeId;
    
    @NotNull(message = "isRead status is required")
    private Boolean isRead;
    
    // `received` can be set by the system, so it's optional in the request
    private String received; // Use String to allow flexibility in input format
}
