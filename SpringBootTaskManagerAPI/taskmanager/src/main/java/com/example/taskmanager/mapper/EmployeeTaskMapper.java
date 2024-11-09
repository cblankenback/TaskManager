package com.example.taskmanager.mapper;

import com.example.taskmanager.dto.EmployeeTaskRequestDTO;
import com.example.taskmanager.dto.EmployeeTaskResponseDTO;
import com.example.taskmanager.entity.EmployeeTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeTaskMapper {

    @Mapping(source = "task.taskId", target = "taskId")
    @Mapping(source = "employee.employeeId", target = "employeeId")
    EmployeeTaskResponseDTO toDTO(EmployeeTask employeeTask);

    // Ignore nested associations to prevent transient entity creation
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "employee", ignore = true)
    EmployeeTask toEntity(EmployeeTaskRequestDTO employeeTaskRequestDTO);
}
