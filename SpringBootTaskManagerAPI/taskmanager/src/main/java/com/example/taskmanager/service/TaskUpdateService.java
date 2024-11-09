package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskUpdateRequestDTO;
import com.example.taskmanager.dto.TaskUpdateResponseDTO;
import com.example.taskmanager.entity.TaskUpdate;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.Comment;
import com.example.taskmanager.entity.Status;
import com.example.taskmanager.entity.Employee;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.TaskUpdateMapper;
import com.example.taskmanager.repository.EmployeeRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.CommentRepository;
import com.example.taskmanager.repository.StatusRepository;
import com.example.taskmanager.repository.TaskUpdateRepository;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(TaskUpdateService.class);

    private final TaskUpdateRepository taskUpdateRepository;
    private final TaskUpdateMapper taskUpdateMapper;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final StatusRepository statusRepository;
    private final EmployeeRepository employeeRepository;

    public TaskUpdateService(TaskUpdateRepository taskUpdateRepository,
                             TaskUpdateMapper taskUpdateMapper,
                             TaskRepository taskRepository,
                             CommentRepository commentRepository,
                             StatusRepository statusRepository,
                             EmployeeRepository employeeRepository) {
        this.taskUpdateRepository = taskUpdateRepository;
        this.taskUpdateMapper = taskUpdateMapper;
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
        this.statusRepository = statusRepository;
        this.employeeRepository = employeeRepository;
    }

    // Retrieve all task updates
    public List<TaskUpdateResponseDTO> getAllTaskUpdates() {
        logger.debug("Fetching all TaskUpdates");
        return taskUpdateRepository.findAll()
                .stream()
                .map(taskUpdateMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Retrieve task update by ID
    public TaskUpdateResponseDTO getTaskUpdateById(Integer id) {
        logger.debug("Fetching TaskUpdate with ID: {}", id);
        return taskUpdateRepository.findById(id)
                .map(taskUpdateMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("TaskUpdate not found with ID: " + id));
    }

    // Create a new task update
    @Transactional
    public TaskUpdateResponseDTO createTaskUpdate(TaskUpdateRequestDTO requestDTO) {
        logger.debug("Creating TaskUpdate with DTO: {}", requestDTO);

        // Map DTO to entity without associations
        TaskUpdate taskUpdate = taskUpdateMapper.toEntity(requestDTO);
        logger.debug("Mapped TaskUpdate entity: {}", taskUpdate);

        // Set updateDate to current time
        taskUpdate.setUpdateDate(LocalDateTime.now());
        logger.debug("Set updateDate to: {}", taskUpdate.getUpdateDate());

        // Fetch and set Task
        Task task = taskRepository.findById(requestDTO.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + requestDTO.getTaskId()));
        taskUpdate.setTask(task);
        logger.debug("Set Task: {}", task);

        // Fetch and set Comment if provided
        if (requestDTO.getCommentId() != null) {
            Comment comment = commentRepository.findById(requestDTO.getCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID: " + requestDTO.getCommentId()));
            taskUpdate.setComment(comment);
            logger.debug("Set Comment: {}", comment);
        } else {
            taskUpdate.setComment(null);
            logger.debug("No Comment ID provided; set Comment to null");
        }

        // Fetch and set Status
        Status status = statusRepository.findById(requestDTO.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found with ID: " + requestDTO.getStatusId()));
        taskUpdate.setStatus(status);
        logger.debug("Set Status: {}", status);

        // Fetch and set Employee
        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + requestDTO.getEmployeeId()));
        taskUpdate.setEmployee(employee);
        logger.debug("Set Employee: {}", employee);

        // Save the entity
        TaskUpdate savedTaskUpdate = taskUpdateRepository.save(taskUpdate);
        logger.debug("Saved TaskUpdate: {}", savedTaskUpdate);

        // Convert to DTO and return
        return taskUpdateMapper.toDTO(savedTaskUpdate);
    }

    // Update an existing task update
    public TaskUpdateResponseDTO updateTaskUpdate(Integer id, TaskUpdateRequestDTO requestDTO) {
        logger.debug("Updating TaskUpdate with ID: {}, DTO: {}", id, requestDTO);

        // Fetch existing TaskUpdate
        TaskUpdate existingTaskUpdate = taskUpdateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskUpdate not found with ID: " + id));

        // Update updateDate to current time
        existingTaskUpdate.setUpdateDate(LocalDateTime.now());
        logger.debug("Updated updateDate to: {}", existingTaskUpdate.getUpdateDate());

        // Update Task if taskId has changed
        if (!existingTaskUpdate.getTask().getTaskId().equals(requestDTO.getTaskId())) {
            Task newTask = taskRepository.findById(requestDTO.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + requestDTO.getTaskId()));
            existingTaskUpdate.setTask(newTask);
            logger.debug("Updated Task to: {}", newTask);
        }

        // Update Comment if commentId has changed
        if (requestDTO.getCommentId() != null) {
            if (existingTaskUpdate.getComment() == null || !existingTaskUpdate.getComment().getCommentId().equals(requestDTO.getCommentId())) {
                Comment newComment = commentRepository.findById(requestDTO.getCommentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID: " + requestDTO.getCommentId()));
                existingTaskUpdate.setComment(newComment);
                logger.debug("Updated Comment to: {}", newComment);
            }
        } else {
            existingTaskUpdate.setComment(null);
            logger.debug("No Comment ID provided; set Comment to null");
        }

        // Update Status if statusId has changed
        if (!existingTaskUpdate.getStatus().getStatusId().equals(requestDTO.getStatusId())) {
            Status newStatus = statusRepository.findById(requestDTO.getStatusId())
                    .orElseThrow(() -> new ResourceNotFoundException("Status not found with ID: " + requestDTO.getStatusId()));
            existingTaskUpdate.setStatus(newStatus);
            logger.debug("Updated Status to: {}", newStatus);
        }

        // Update Employee if employeeId has changed
        if (!existingTaskUpdate.getEmployee().getEmployeeId().equals(requestDTO.getEmployeeId())) {
            Employee newEmployee = employeeRepository.findById(requestDTO.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + requestDTO.getEmployeeId()));
            existingTaskUpdate.setEmployee(newEmployee);
            logger.debug("Updated Employee to: {}", newEmployee);
        }

        // Save the updated entity
        TaskUpdate updatedTaskUpdate = taskUpdateRepository.save(existingTaskUpdate);
        logger.debug("Saved updated TaskUpdate: {}", updatedTaskUpdate);

        // Convert to DTO and return
        return taskUpdateMapper.toDTO(updatedTaskUpdate);
    }

    // Delete a task update
    public void deleteTaskUpdate(Integer id) {
        logger.debug("Deleting TaskUpdate with ID: {}", id);
        TaskUpdate existingTaskUpdate = taskUpdateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskUpdate not found with ID: " + id));
        taskUpdateRepository.delete(existingTaskUpdate);
        logger.debug("Deleted TaskUpdate with ID: {}", id);
    }
}
