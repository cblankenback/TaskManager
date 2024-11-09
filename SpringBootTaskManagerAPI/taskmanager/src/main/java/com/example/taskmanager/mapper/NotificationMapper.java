package com.example.taskmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.taskmanager.dto.NotificationRequestDTO;
import com.example.taskmanager.dto.NotificationResponseDTO;
import com.example.taskmanager.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
	
	
	// Notification Comment entity to CommentResponseDTO
	@Mapping(source = "task.taskId", target = "taskId")
	NotificationResponseDTO toDTO(Notification notification);
    
    // Notification CommentRequestDTO to Comment entity
	@Mapping(source = "taskId", target = "task.taskId") 
	Notification toEntity(NotificationRequestDTO notificationRequestDTO);
}
