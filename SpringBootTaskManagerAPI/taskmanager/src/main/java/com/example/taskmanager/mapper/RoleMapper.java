package com.example.taskmanager.mapper;

import org.mapstruct.Mapper;

import com.example.taskmanager.dto.RoleRequestDTO;
import com.example.taskmanager.dto.RoleResponseDTO;
import com.example.taskmanager.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

	RoleResponseDTO toDTO(Role role);
	    
	    // Convert AvailbilityRequestDTO to Availbility entity
	Role toEntity(RoleRequestDTO RoleRequestDTO);
}
