package com.example.taskmanager;

import com.example.taskmanager.dto.TaskUpdateRequestDTO;
import com.example.taskmanager.dto.TaskUpdateResponseDTO;
import com.example.taskmanager.entity.*;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.TaskUpdateMapper;
import com.example.taskmanager.repository.*;
import com.example.taskmanager.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskUpdateServiceTest {

    @Mock
    private TaskUpdateRepository taskUpdateRepository;

    @Mock
    private TaskUpdateMapper taskUpdateMapper;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private TaskUpdateService taskUpdateService;

    private TaskUpdateRequestDTO taskUpdateRequestDTO;
    private TaskUpdate taskUpdate;
    private TaskUpdateResponseDTO taskUpdateResponseDTO;
    private Task task;
    private Status status;
    private Employee employee;

    @BeforeEach
    public void setUp() {
        // Removed MockitoAnnotations.openMocks(this);

        // Set up test data
        taskUpdateRequestDTO = new TaskUpdateRequestDTO();
        taskUpdateRequestDTO.setTaskId(1);
        taskUpdateRequestDTO.setComment("Test comment");
        taskUpdateRequestDTO.setStatusId(1);
        taskUpdateRequestDTO.setEmployeeId(1);

        task = new Task();
        task.setTaskId(1);
        task.setTaskName("Sample Task");

        status = new Status();
        status.setStatusId(1);
        status.setStatusName("In Progress");

        employee = new Employee();
        employee.setEmployeeId(1);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        taskUpdate = new TaskUpdate();
        taskUpdate.setTaskUpdateId(1);
        taskUpdate.setUpdateDate(LocalDateTime.now());
        taskUpdate.setTask(task);
        taskUpdate.setComment("Test comment");
        taskUpdate.setStatus(status);
        taskUpdate.setEmployee(employee);

        taskUpdateResponseDTO = new TaskUpdateResponseDTO();
        taskUpdateResponseDTO.setTaskUpdateId(1);
        taskUpdateResponseDTO.setUpdateDate(taskUpdate.getUpdateDate());
        taskUpdateResponseDTO.setTaskId(1);
        taskUpdateResponseDTO.setComment("Test comment");
        taskUpdateResponseDTO.setStatusId(1);
        taskUpdateResponseDTO.setEmployeeId(1);
    }

    @Test
    public void testGetAllTaskUpdates() {
        // Arrange
        when(taskUpdateRepository.findAll()).thenReturn(Arrays.asList(taskUpdate));
        when(taskUpdateMapper.toDTO(any(TaskUpdate.class))).thenReturn(taskUpdateResponseDTO);

        // Act
        List<TaskUpdateResponseDTO> result = taskUpdateService.getAllTaskUpdates();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(taskUpdateResponseDTO, result.get(0));
        verify(taskUpdateRepository, times(1)).findAll();
        verify(taskUpdateMapper, times(1)).toDTO(any(TaskUpdate.class));
    }

    @Test
    public void testGetTaskUpdateById_Found() {
        // Arrange
        when(taskUpdateRepository.findById(1)).thenReturn(Optional.of(taskUpdate));
        when(taskUpdateMapper.toDTO(taskUpdate)).thenReturn(taskUpdateResponseDTO);

        // Act
        TaskUpdateResponseDTO result = taskUpdateService.getTaskUpdateById(1);

        // Assert
        assertNotNull(result);
        assertEquals(taskUpdateResponseDTO, result);
        verify(taskUpdateRepository, times(1)).findById(1);
        verify(taskUpdateMapper, times(1)).toDTO(taskUpdate);
    }

    @Test
    public void testGetTaskUpdateById_NotFound() {
        // Arrange
        when(taskUpdateRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> taskUpdateService.getTaskUpdateById(1));
        verify(taskUpdateRepository, times(1)).findById(1);
        verifyNoMoreInteractions(taskUpdateMapper);
    }

    @Test
    public void testCreateTaskUpdate_Success() {
        // Arrange
        when(taskUpdateMapper.toEntity(taskUpdateRequestDTO)).thenReturn(new TaskUpdate());
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        when(statusRepository.findById(1)).thenReturn(Optional.of(status));
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(taskUpdateRepository.save(any(TaskUpdate.class))).thenReturn(taskUpdate);
        when(taskUpdateMapper.toDTO(taskUpdate)).thenReturn(taskUpdateResponseDTO);

        // Act
        TaskUpdateResponseDTO result = taskUpdateService.createTaskUpdate(taskUpdateRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(taskUpdateResponseDTO, result);
        verify(taskUpdateMapper, times(1)).toEntity(taskUpdateRequestDTO);
        verify(taskRepository, times(1)).findById(1);
        verify(statusRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).findById(1);
        verify(taskUpdateRepository, times(1)).save(any(TaskUpdate.class));
        verify(taskUpdateMapper, times(1)).toDTO(taskUpdate);
    }

    @Test
    public void testCreateTaskUpdate_TaskNotFound() {
        // Arrange
        when(taskUpdateMapper.toEntity(taskUpdateRequestDTO)).thenReturn(new TaskUpdate());
        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> taskUpdateService.createTaskUpdate(taskUpdateRequestDTO));
        verify(taskUpdateMapper, times(1)).toEntity(taskUpdateRequestDTO);
        verify(taskRepository, times(1)).findById(1);
        verifyNoMoreInteractions(statusRepository, employeeRepository, taskUpdateRepository, taskUpdateMapper);
    }

    @Test
    public void testUpdateTaskUpdate_Success() {
        // Arrange
        TaskUpdate existingTaskUpdate = new TaskUpdate();
        existingTaskUpdate.setTaskUpdateId(1);
        existingTaskUpdate.setTask(task); // taskId = 1
        existingTaskUpdate.setStatus(status); // statusId = 1
        existingTaskUpdate.setEmployee(employee); // employeeId = 1
        existingTaskUpdate.setComment("Old comment");

        // Keep the same IDs in requestDTO
        taskUpdateRequestDTO.setTaskId(1);
        taskUpdateRequestDTO.setStatusId(1);
        taskUpdateRequestDTO.setEmployeeId(1);
        taskUpdateRequestDTO.setComment("Updated comment");

        when(taskUpdateRepository.findById(1)).thenReturn(Optional.of(existingTaskUpdate));
        when(taskUpdateRepository.save(existingTaskUpdate)).thenReturn(existingTaskUpdate);
        when(taskUpdateMapper.toDTO(existingTaskUpdate)).thenReturn(taskUpdateResponseDTO);

        // Act
        TaskUpdateResponseDTO result = taskUpdateService.updateTaskUpdate(1, taskUpdateRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(taskUpdateResponseDTO, result);
        verify(taskUpdateRepository, times(1)).findById(1);
        verify(taskRepository, times(0)).findById(anyInt()); // Task ID did not change
        verify(statusRepository, times(0)).findById(anyInt()); // Status ID did not change
        verify(employeeRepository, times(0)).findById(anyInt()); // Employee ID did not change
        verify(taskUpdateRepository, times(1)).save(existingTaskUpdate);
        verify(taskUpdateMapper, times(1)).toDTO(existingTaskUpdate);
    }

    @Test
    public void testUpdateTaskUpdate_NotFound() {
        // Arrange
        when(taskUpdateRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> taskUpdateService.updateTaskUpdate(1, taskUpdateRequestDTO));
        verify(taskUpdateRepository, times(1)).findById(1);
        verifyNoMoreInteractions(taskRepository, statusRepository, employeeRepository, taskUpdateRepository, taskUpdateMapper);
    }

    @Test
    public void testDeleteTaskUpdate_Success() {
        // Arrange
        when(taskUpdateRepository.findById(1)).thenReturn(Optional.of(taskUpdate));

        // Act
        taskUpdateService.deleteTaskUpdate(1);

        // Assert
        verify(taskUpdateRepository, times(1)).findById(1);
        verify(taskUpdateRepository, times(1)).delete(taskUpdate);
    }

    @Test
    public void testDeleteTaskUpdate_NotFound() {
        // Arrange
        when(taskUpdateRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> taskUpdateService.deleteTaskUpdate(1));
        verify(taskUpdateRepository, times(1)).findById(1);
        verify(taskUpdateRepository, times(0)).delete(any(TaskUpdate.class));
    }
}
