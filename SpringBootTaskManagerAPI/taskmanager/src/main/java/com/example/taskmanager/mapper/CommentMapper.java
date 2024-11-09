package com.example.taskmanager.mapper;

import org.mapstruct.Mapper;

import com.example.taskmanager.dto.CommentResponseDTO;
import com.example.taskmanager.dto.CommentRequestDTO;

import com.example.taskmanager.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
	// Convert Comment entity to CommentResponseDTO
	
    CommentResponseDTO toDTO(Comment comment);
    
    // Convert CommentRequestDTO to Comment entity
    Comment toEntity(CommentRequestDTO commentRequestDTO);
}
