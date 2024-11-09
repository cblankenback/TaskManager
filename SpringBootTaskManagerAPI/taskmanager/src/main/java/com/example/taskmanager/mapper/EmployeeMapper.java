package com.example.taskmanager.mapper;

import com.example.taskmanager.dto.EmployeeRequestDTO;
import com.example.taskmanager.dto.EmployeeResponseDTO;
import com.example.taskmanager.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    // Convert Employee entity to EmployeeResponseDTO
    @Mapping(source = "department.departmentId", target = "departmentId")
    @Mapping(source = "availability.availabilityId", target = "availabilityId")
    @Mapping(source = "role.roleId", target = "roleId")
    // Remove or comment out the line below
    // @Mapping(target = "password", ignore = true)
    EmployeeResponseDTO toDTO(Employee employee);

    // Convert EmployeeRequestDTO to Employee entity
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "availability", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdTasks", ignore = true)
    Employee toEntity(EmployeeRequestDTO employeeRequestDTO);
}
