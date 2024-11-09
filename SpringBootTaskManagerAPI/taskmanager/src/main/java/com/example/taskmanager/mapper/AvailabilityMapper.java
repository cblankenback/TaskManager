package com.example.taskmanager.mapper;

import org.mapstruct.Mapper;

import com.example.taskmanager.dto.AvailabilityRequestDTO;
import com.example.taskmanager.dto.AvailabilityResponseDTO;
import com.example.taskmanager.entity.Availability;

@Mapper(componentModel = "spring")
public interface AvailabilityMapper {

	AvailabilityResponseDTO toDTO(Availability availbility);
	    
	    // Convert AvailbilityRequestDTO to Availbility entity
	Availability toEntity(AvailabilityRequestDTO AvailbilityRequestDTO);
}
