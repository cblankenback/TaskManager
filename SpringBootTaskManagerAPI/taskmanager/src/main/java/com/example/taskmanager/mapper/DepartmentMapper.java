package com.example.taskmanager.mapper;

import org.mapstruct.Mapper;

import com.example.taskmanager.dto.DepartmentRequestDTO;
import com.example.taskmanager.dto.DepartmentResponseDTO;
import com.example.taskmanager.entity.Department;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

	DepartmentResponseDTO toDTO(Department department);
	    
	    // Convert AvailbilityRequestDTO to Availbility entity
	Department toEntity(DepartmentRequestDTO departmentRequestDTO);
}
