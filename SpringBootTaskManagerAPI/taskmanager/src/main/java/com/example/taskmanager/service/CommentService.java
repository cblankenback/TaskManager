package com.example.taskmanager.service;

import com.example.taskmanager.dto.CommentRequestDTO;
import com.example.taskmanager.dto.CommentResponseDTO;
import com.example.taskmanager.entity.Comment;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.CommentMapper;
import com.example.taskmanager.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository,
                          CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    // Retrieve all comments
    public List<CommentResponseDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                       .map(commentMapper::toDTO)
                       .collect(Collectors.toList());
    }

    // Retrieve a comment by ID
    public CommentResponseDTO getCommentById(Integer id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID: " + id));
        return commentMapper.toDTO(comment);
    }

    // Create a new comment
    public CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO) {
        Comment comment = commentMapper.toEntity(commentRequestDTO);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDTO(savedComment);
    }

    // Update an existing comment
    public CommentResponseDTO updateComment(Integer id, CommentRequestDTO commentRequestDTO) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID: " + id));
        
        existingComment.setMessage(commentRequestDTO.getMessage());
        // Update other fields if there are any

        Comment updatedComment = commentRepository.save(existingComment);
        return commentMapper.toDTO(updatedComment);
    }

    // Delete a comment
    public void deleteComment(Integer id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID: " + id));
        commentRepository.delete(existingComment);
    }
}
