package com.example.taskmanager.mapper;

import com.example.taskmanager.dto.EmployeeNotificationRequestDTO;
import com.example.taskmanager.dto.EmployeeNotificationResponseDTO;
import com.example.taskmanager.entity.EmployeeNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeNotificationMapper {

    // Convert EmployeeNotification entity to EmployeeNotificationResponseDTO
    @Mapping(source = "notification.notificationId", target = "notificationId")
    @Mapping(source = "employee.employeeId", target = "employeeId")
    EmployeeNotificationResponseDTO toDTO(EmployeeNotification employeeNotification);
    
    // Convert EmployeeNotificationRequestDTO to EmployeeNotification entity
    @Mapping(source = "notificationId", target = "notification.notificationId")
    @Mapping(source = "employeeId", target = "employee.employeeId")
    @Mapping(target = "received", ignore = true) // This should be set at the time of creation or update, not from DTO
    EmployeeNotification toEntity(EmployeeNotificationRequestDTO requestDTO);
}
