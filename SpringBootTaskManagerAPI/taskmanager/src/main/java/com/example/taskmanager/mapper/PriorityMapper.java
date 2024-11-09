package com.example.taskmanager.mapper;

import org.mapstruct.Mapper;
import com.example.taskmanager.dto.PriorityRequestDTO;
import com.example.taskmanager.dto.PriorityResponseDTO;
import com.example.taskmanager.entity.Priority;

@Mapper(componentModel = "spring")
public interface PriorityMapper {
    PriorityResponseDTO toDTO(Priority priority);
    Priority toEntity(PriorityRequestDTO priorityRequestDTO);
}
