package com.example.taskmanager.mapper;

import org.mapstruct.Mapper;

import com.example.taskmanager.dto.StatusRequestDTO;
import com.example.taskmanager.dto.StatusResponseDTO;
import com.example.taskmanager.entity.Status;

@Mapper(componentModel = "spring")
public interface StatusMapper {

	
	 StatusResponseDTO toDTO(Status status);
	    
	    // Convert StatusRequestDTO to Status entity
	    Status toEntity(StatusRequestDTO statusRequestDTO);
}
