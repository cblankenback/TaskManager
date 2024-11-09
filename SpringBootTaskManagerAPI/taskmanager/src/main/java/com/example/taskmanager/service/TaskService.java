package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequestDTO;
import com.example.taskmanager.dto.TaskResponseDTO;
import com.example.taskmanager.entity.Employee;
import com.example.taskmanager.entity.Priority;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.repository.EmployeeRepository;
import com.example.taskmanager.repository.PriorityRepository;
import com.example.taskmanager.repository.TaskRepository;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final EmployeeRepository employeeRepository;
    private final PriorityRepository priorityRepository;

    public TaskService(TaskRepository taskRepository,
                       TaskMapper taskMapper,
                       EmployeeRepository employeeRepository,
                       PriorityRepository priorityRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.employeeRepository = employeeRepository;
        this.priorityRepository = priorityRepository;
    }

    // Retrieve all tasks
    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Retrieve task by ID
    public TaskResponseDTO getTaskById(Integer id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));
    }

    // Create a new task
    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        logger.debug("Creating Task with DTO: {}", taskRequestDTO);

        // Map DTO to Entity without setting associations
        Task task = taskMapper.toEntity(taskRequestDTO);
        logger.debug("Mapped Task entity: {}", task);

        // Fetch and set the Employee (createdBy)
        Integer createdById = taskRequestDTO.getCreatedById();
        logger.debug("Fetching Employee with ID: {}", createdById);
        Employee createdBy = employeeRepository.findById(createdById)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + createdById));
        task.setCreatedBy(createdBy);
        logger.debug("Set createdBy: {}", createdBy);

        // Fetch and set Priority if provided
        if (taskRequestDTO.getPriorityId() != null) {
            Integer priorityId = taskRequestDTO.getPriorityId();
            logger.debug("Fetching Priority with ID: {}", priorityId);
            Priority priority = priorityRepository.findById(priorityId)
                .orElseThrow(() -> new ResourceNotFoundException("Priority not found with ID: " + priorityId));
            task.setPriority(priority);
            logger.debug("Set priority: {}", priority);
        }

        // Fetch and set dependency task if provided
        if (taskRequestDTO.getDependencyTaskId() != null) {
            Integer dependencyTaskId = taskRequestDTO.getDependencyTaskId();
            logger.debug("Fetching DependencyTask with ID: {}", dependencyTaskId);
            Task dependencyTask = taskRepository.findById(dependencyTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Dependency Task not found with ID: " + dependencyTaskId));
            task.setDependencyTask(dependencyTask);
            logger.debug("Set dependencyTask: {}", dependencyTask);
        } else {
            logger.debug("No DependencyTask ID provided; setting dependencyTask to null");
            task.setDependencyTask(null);
        }

        Task savedTask = taskRepository.save(task);
        logger.debug("Saved Task: {}", savedTask);
        return taskMapper.toDTO(savedTask);
    }

    // Update an existing task
    public TaskResponseDTO updateTask(Integer id, TaskRequestDTO taskRequestDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));

        // Update fields
        existingTask.setTaskName(taskRequestDTO.getTaskName());
        existingTask.setDescription(taskRequestDTO.getDescription());
        existingTask.setDeadline(taskRequestDTO.getDeadline());

        // Update Priority if provided
        if (taskRequestDTO.getPriorityId() != null) {
            Integer priorityId = taskRequestDTO.getPriorityId();
            logger.debug("Fetching Priority with ID: {}", priorityId);
            Priority priority = priorityRepository.findById(priorityId)
                .orElseThrow(() -> new ResourceNotFoundException("Priority not found with ID: " + priorityId));
            existingTask.setPriority(priority);
            logger.debug("Set priority: {}", priority);
        } else {
            existingTask.setPriority(null);
            logger.debug("Priority set to null");
        }

        // Update Dependency Task if provided
        if (taskRequestDTO.getDependencyTaskId() != null) {
            Integer dependencyTaskId = taskRequestDTO.getDependencyTaskId();
            logger.debug("Fetching DependencyTask with ID: {}", dependencyTaskId);
            Task dependencyTask = taskRepository.findById(dependencyTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Dependency Task not found with ID: " + dependencyTaskId));
            existingTask.setDependencyTask(dependencyTask);
            logger.debug("Set dependencyTask: {}", dependencyTask);
        } else {
            existingTask.setDependencyTask(null);
            logger.debug("DependencyTask set to null");
        }

        Task updatedTask = taskRepository.save(existingTask);
        logger.debug("Updated Task: {}", updatedTask);
        return taskMapper.toDTO(updatedTask);
    }

    // Delete a task
    public void deleteTask(Integer id) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));
        taskRepository.delete(existingTask);
        logger.debug("Deleted Task with ID: {}", id);
    }
}
