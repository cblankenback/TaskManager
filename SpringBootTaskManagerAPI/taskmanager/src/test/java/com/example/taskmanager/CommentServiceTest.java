package com.example.taskmanager;


import com.example.taskmanager.service.CommentService;
import com.example.taskmanager.dto.CommentRequestDTO;
import com.example.taskmanager.dto.CommentResponseDTO;
import com.example.taskmanager.entity.Comment;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.CommentMapper;
import com.example.taskmanager.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllComments() {
        Comment comment1 = new Comment(1, "First comment");
        Comment comment2 = new Comment(2, "Second comment");
        List<Comment> comments = Arrays.asList(comment1, comment2);

        CommentResponseDTO dto1 = new CommentResponseDTO();
        dto1.setCommentId(1);
        dto1.setMessage("First comment");

        CommentResponseDTO dto2 = new CommentResponseDTO();
        dto2.setCommentId(2);
        dto2.setMessage("Second comment");

        when(commentRepository.findAll()).thenReturn(comments);
        when(commentMapper.toDTO(comment1)).thenReturn(dto1);
        when(commentMapper.toDTO(comment2)).thenReturn(dto2);

        List<CommentResponseDTO> result = commentService.getAllComments();

        assertEquals(2, result.size());
        assertEquals("First comment", result.get(0).getMessage());
        assertEquals("Second comment", result.get(1).getMessage());

        verify(commentRepository, times(1)).findAll();
        verify(commentMapper, times(1)).toDTO(comment1);
        verify(commentMapper, times(1)).toDTO(comment2);
    }

    @Test
    public void testGetCommentById_Success() {
        Comment comment = new Comment(1, "Sample comment");
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setCommentId(1);
        dto.setMessage("Sample comment");

        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(commentMapper.toDTO(comment)).thenReturn(dto);

        CommentResponseDTO result = commentService.getCommentById(1);

        assertNotNull(result);
        assertEquals("Sample comment", result.getMessage());

        verify(commentRepository, times(1)).findById(1);
        verify(commentMapper, times(1)).toDTO(comment);
    }

    @Test
    public void testGetCommentById_NotFound() {
        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.getCommentById(1);
        });

        verify(commentRepository, times(1)).findById(1);
        verify(commentMapper, times(0)).toDTO(any());
    }

    @Test
    public void testCreateComment() {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setMessage("New comment");

        Comment comment = new Comment();
        comment.setMessage("New comment");

        Comment savedComment = new Comment(1, "New comment");

        CommentResponseDTO responseDTO = new CommentResponseDTO();
        responseDTO.setCommentId(1);
        responseDTO.setMessage("New comment");

        when(commentMapper.toEntity(requestDTO)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(savedComment);
        when(commentMapper.toDTO(savedComment)).thenReturn(responseDTO);

        CommentResponseDTO result = commentService.createComment(requestDTO);

        assertNotNull(result);
        assertEquals(1, result.getCommentId());
        assertEquals("New comment", result.getMessage());

        verify(commentMapper, times(1)).toEntity(requestDTO);
        verify(commentRepository, times(1)).save(comment);
        verify(commentMapper, times(1)).toDTO(savedComment);
    }

    @Test
    public void testUpdateComment_Success() {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setMessage("Updated comment");

        Comment existingComment = new Comment(1, "Old comment");
        Comment updatedComment = new Comment(1, "Updated comment");
        CommentResponseDTO responseDTO = new CommentResponseDTO();
        responseDTO.setCommentId(1);
        responseDTO.setMessage("Updated comment");

        when(commentRepository.findById(1)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(existingComment)).thenReturn(updatedComment);
        when(commentMapper.toDTO(updatedComment)).thenReturn(responseDTO);

        CommentResponseDTO result = commentService.updateComment(1, requestDTO);

        assertNotNull(result);
        assertEquals(1, result.getCommentId());
        assertEquals("Updated comment", result.getMessage());

        verify(commentRepository, times(1)).findById(1);
        verify(commentRepository, times(1)).save(existingComment);
        verify(commentMapper, times(1)).toDTO(updatedComment);
    }

    @Test
    public void testUpdateComment_NotFound() {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setMessage("Updated comment");

        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.updateComment(1, requestDTO);
        });

        verify(commentRepository, times(1)).findById(1);
        verify(commentRepository, times(0)).save(any());
        verify(commentMapper, times(0)).toDTO(any());
    }

    @Test
    public void testDeleteComment_Success() {
        Comment existingComment = new Comment(1, "Sample comment");

        when(commentRepository.findById(1)).thenReturn(Optional.of(existingComment));
        doNothing().when(commentRepository).delete(existingComment);

        commentService.deleteComment(1);

        verify(commentRepository, times(1)).findById(1);
        verify(commentRepository, times(1)).delete(existingComment);
    }

    @Test
    public void testDeleteComment_NotFound() {
        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.deleteComment(1);
        });

        verify(commentRepository, times(1)).findById(1);
        verify(commentRepository, times(0)).delete(any());
    }
}
