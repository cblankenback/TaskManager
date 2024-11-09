package com.example.taskmanager;

import com.example.taskmanager.dto.EmployeeTaskRequestDTO;
import com.example.taskmanager.dto.EmployeeTaskResponseDTO;
import com.example.taskmanager.entity.Employee;
import com.example.taskmanager.entity.EmployeeTask;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.EmployeeTaskMapper;
import com.example.taskmanager.repository.EmployeeRepository;
import com.example.taskmanager.repository.EmployeeTaskRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.service.EmployeeTaskService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeTaskServiceTest {

    @Mock
    private EmployeeTaskRepository employeeTaskRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private EmployeeTaskMapper employeeTaskMapper;

    @InjectMocks
    private EmployeeTaskService employeeTaskService;

    @Test
    void testCreateEmployeeTask_Success() {
        // Given
        EmployeeTaskRequestDTO requestDTO = new EmployeeTaskRequestDTO();
        requestDTO.setEmployeeId(1);
        requestDTO.setTaskId(1);
        requestDTO.setAssignedDate(LocalDateTime.now());

        Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        Task task = new Task();
        task.setTaskId(1);
        task.setTaskName("Sample Task");

        EmployeeTask employeeTaskEntity = new EmployeeTask();
        employeeTaskEntity.setEmployee(employee);
        employeeTaskEntity.setTask(task);
        employeeTaskEntity.setAssignedDate(requestDTO.getAssignedDate());

        EmployeeTask savedEmployeeTask = new EmployeeTask();
        savedEmployeeTask.setEmployeeTaskId(1);
        savedEmployeeTask.setEmployee(employee);
        savedEmployeeTask.setTask(task);
        savedEmployeeTask.setAssignedDate(requestDTO.getAssignedDate());

        EmployeeTaskResponseDTO responseDTO = new EmployeeTaskResponseDTO();
        responseDTO.setEmployeeTaskId(1);
        responseDTO.setEmployeeId(1);
        responseDTO.setTaskId(1);
        responseDTO.setAssignedDate(requestDTO.getAssignedDate());

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        when(employeeTaskMapper.toEntity(requestDTO)).thenReturn(employeeTaskEntity);
        when(employeeTaskRepository.save(employeeTaskEntity)).thenReturn(savedEmployeeTask);
        when(employeeTaskMapper.toDTO(savedEmployeeTask)).thenReturn(responseDTO);

        // When
        EmployeeTaskResponseDTO result = employeeTaskService.createEmployeeTask(requestDTO);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getEmployeeTaskId());
        assertEquals(1, result.getEmployeeId());
        assertEquals(1, result.getTaskId());
        assertEquals(requestDTO.getAssignedDate(), result.getAssignedDate());

        verify(employeeRepository, times(1)).findById(1);
        verify(taskRepository, times(1)).findById(1);
        verify(employeeTaskMapper, times(1)).toEntity(requestDTO);
        verify(employeeTaskRepository, times(1)).save(employeeTaskEntity);
        verify(employeeTaskMapper, times(1)).toDTO(savedEmployeeTask);
    }
}
