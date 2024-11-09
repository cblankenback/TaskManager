package com.example.taskmanager.service;

import com.example.taskmanager.dto.EmployeeTaskRequestDTO;
import com.example.taskmanager.dto.EmployeeTaskResponseDTO;
import com.example.taskmanager.entity.EmployeeTask;
import com.example.taskmanager.entity.Employee;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.EmployeeTaskMapper;
import com.example.taskmanager.repository.EmployeeRepository;
import com.example.taskmanager.repository.EmployeeTaskRepository;
import com.example.taskmanager.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeTaskService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeTaskService.class);

    private final EmployeeTaskRepository employeeTaskRepository;
    private final EmployeeTaskMapper employeeTaskMapper;
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;

    public EmployeeTaskService(EmployeeTaskRepository employeeTaskRepository,
                               EmployeeTaskMapper employeeTaskMapper,
                               EmployeeRepository employeeRepository,
                               TaskRepository taskRepository) {
        this.employeeTaskRepository = employeeTaskRepository;
        this.employeeTaskMapper = employeeTaskMapper;
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
    }

    // Retrieve all employee tasks
    public List<EmployeeTaskResponseDTO> getAllEmployeeTasks() {
        logger.debug("Fetching all EmployeeTasks");
        return employeeTaskRepository.findAll()
                .stream()
                .map(employeeTaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Retrieve employee task by ID
    public EmployeeTaskResponseDTO getEmployeeTaskById(Integer id) {
        logger.debug("Fetching EmployeeTask with ID: {}", id);
        return employeeTaskRepository.findById(id)
                .map(employeeTaskMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeTask not found with ID: " + id));
    }

    // Create a new employee task
    @Transactional
    public EmployeeTaskResponseDTO createEmployeeTask(EmployeeTaskRequestDTO requestDTO) {
        logger.debug("Creating EmployeeTask with DTO: {}", requestDTO);

        // Map DTO to entity without associations
        EmployeeTask employeeTask = employeeTaskMapper.toEntity(requestDTO);
        logger.debug("Mapped EmployeeTask entity: {}", employeeTask);

        // Fetch and set Task
        Task task = taskRepository.findById(requestDTO.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + requestDTO.getTaskId()));
        employeeTask.setTask(task);
        logger.debug("Set Task: {}", task);

        // Fetch and set Employee
        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + requestDTO.getEmployeeId()));
        employeeTask.setEmployee(employee);
        logger.debug("Set Employee: {}", employee);

        // Save the entity
        EmployeeTask savedEmployeeTask = employeeTaskRepository.save(employeeTask);
        logger.debug("Saved EmployeeTask: {}", savedEmployeeTask);

        // Convert to DTO and return
        return employeeTaskMapper.toDTO(savedEmployeeTask);
    }

    // Update an existing employee task
    public EmployeeTaskResponseDTO updateEmployeeTask(Integer id, EmployeeTaskRequestDTO requestDTO) {
        logger.debug("Updating EmployeeTask with ID: {}, DTO: {}", id, requestDTO);

        // Fetch existing EmployeeTask
        EmployeeTask existingEmployeeTask = employeeTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeTask not found with ID: " + id));

        // Update assignedDate if provided
        existingEmployeeTask.setAssignedDate(requestDTO.getAssignedDate());
        logger.debug("Updated assignedDate to: {}", requestDTO.getAssignedDate());

        // Update Task if taskId has changed
        if (!existingEmployeeTask.getTask().getTaskId().equals(requestDTO.getTaskId())) {
            Task newTask = taskRepository.findById(requestDTO.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + requestDTO.getTaskId()));
            existingEmployeeTask.setTask(newTask);
            logger.debug("Updated Task to: {}", newTask);
        }

        // Update Employee if employeeId has changed
        if (!existingEmployeeTask.getEmployee().getEmployeeId().equals(requestDTO.getEmployeeId())) {
            Employee newEmployee = employeeRepository.findById(requestDTO.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + requestDTO.getEmployeeId()));
            existingEmployeeTask.setEmployee(newEmployee);
            logger.debug("Updated Employee to: {}", newEmployee);
        }

        // Save the updated entity
        EmployeeTask updatedEmployeeTask = employeeTaskRepository.save(existingEmployeeTask);
        logger.debug("Saved updated EmployeeTask: {}", updatedEmployeeTask);

        // Convert to DTO and return
        return employeeTaskMapper.toDTO(updatedEmployeeTask);
    }

    // Delete an employee task
    public void deleteEmployeeTask(Integer id) {
        logger.debug("Deleting EmployeeTask with ID: {}", id);
        EmployeeTask existingEmployeeTask = employeeTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeTask not found with ID: " + id));
        employeeTaskRepository.delete(existingEmployeeTask);
        logger.debug("Deleted EmployeeTask with ID: {}", id);
    }
}
