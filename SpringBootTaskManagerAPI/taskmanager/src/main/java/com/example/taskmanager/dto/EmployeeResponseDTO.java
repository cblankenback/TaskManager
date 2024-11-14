package com.example.taskmanager.dto;

import lombok.Data;

@Data
public class EmployeeResponseDTO {

    private Integer employeeId;
    
    private String username;

    private String firstName;

    private String lastName;

    private Integer departmentId;

    private Integer availabilityId;

    private Integer roleId;
}
