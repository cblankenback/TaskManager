package com.example.taskmanager.service;

import com.example.taskmanager.dto.EmployeeNotificationRequestDTO;
import com.example.taskmanager.dto.EmployeeNotificationResponseDTO;
import com.example.taskmanager.entity.EmployeeNotification;
import com.example.taskmanager.entity.Employee;
import com.example.taskmanager.entity.Notification;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.EmployeeNotificationMapper;
import com.example.taskmanager.repository.EmployeeNotificationRepository;
import com.example.taskmanager.repository.EmployeeRepository;
import com.example.taskmanager.repository.NotificationRepository;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeNotificationService.class);

    private final EmployeeNotificationRepository employeeNotificationRepository;
    private final EmployeeNotificationMapper employeeNotificationMapper;
    private final EmployeeRepository employeeRepository;
    private final NotificationRepository notificationRepository;

    public EmployeeNotificationService(EmployeeNotificationRepository employeeNotificationRepository,
                                       EmployeeNotificationMapper employeeNotificationMapper,
                                       EmployeeRepository employeeRepository,
                                       NotificationRepository notificationRepository) {
        this.employeeNotificationRepository = employeeNotificationRepository;
        this.employeeNotificationMapper = employeeNotificationMapper;
        this.employeeRepository = employeeRepository;
        this.notificationRepository = notificationRepository;
    }

    // Retrieve all employee notifications
    public List<EmployeeNotificationResponseDTO> getAllEmployeeNotifications() {
        logger.debug("Fetching all employee notifications");
        return employeeNotificationRepository.findAll()
                .stream()
                .map(employeeNotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Retrieve employee notification by ID
    public EmployeeNotificationResponseDTO getEmployeeNotificationById(Integer id) {
        logger.debug("Fetching EmployeeNotification with ID: {}", id);
        return employeeNotificationRepository.findById(id)
                .map(employeeNotificationMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeNotification not found with ID: " + id));
    }

    // Create a new employee notification
    @Transactional
    public EmployeeNotificationResponseDTO createEmployeeNotification(EmployeeNotificationRequestDTO requestDTO) {
        logger.debug("Creating EmployeeNotification with DTO: {}", requestDTO);

        EmployeeNotification notification = employeeNotificationMapper.toEntity(requestDTO);
        notification.setReceived(LocalDateTime.now()); // Set the received time at creation

        // Fetch and set the associated Notification
        Notification fetchedNotification = notificationRepository.findById(requestDTO.getNotificationId())
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + requestDTO.getNotificationId()));
        notification.setNotification(fetchedNotification);
        logger.debug("Set Notification: {}", fetchedNotification);

        // Fetch and set the associated Employee
        Employee fetchedEmployee = employeeRepository.findById(requestDTO.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + requestDTO.getEmployeeId()));
        notification.setEmployee(fetchedEmployee);
        logger.debug("Set Employee: {}", fetchedEmployee);

        EmployeeNotification savedNotification = employeeNotificationRepository.save(notification);
        logger.debug("Saved EmployeeNotification: {}", savedNotification);
        return employeeNotificationMapper.toDTO(savedNotification);
    }

    // Update an existing employee notification
    public EmployeeNotificationResponseDTO updateEmployeeNotification(Integer id, EmployeeNotificationRequestDTO requestDTO) {
        logger.debug("Updating EmployeeNotification with ID: {}, DTO: {}", id, requestDTO);

        EmployeeNotification existingNotification = employeeNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeNotification not found with ID: " + id));

        existingNotification.setIsRead(requestDTO.getIsRead());
        existingNotification.setReceived(LocalDateTime.now()); // Update the received time on update

        if (!existingNotification.getNotification().getNotificationId().equals(requestDTO.getNotificationId())) {
            Notification notification = notificationRepository.findById(requestDTO.getNotificationId())
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + requestDTO.getNotificationId()));
            existingNotification.setNotification(notification);
            logger.debug("Updated Notification: {}", notification);
        }

        if (!existingNotification.getEmployee().getEmployeeId().equals(requestDTO.getEmployeeId())) {
            Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + requestDTO.getEmployeeId()));
            existingNotification.setEmployee(employee);
            logger.debug("Updated Employee: {}", employee);
        }

        EmployeeNotification updatedNotification = employeeNotificationRepository.save(existingNotification);
        logger.debug("Updated EmployeeNotification: {}", updatedNotification);
        return employeeNotificationMapper.toDTO(updatedNotification);
    }

    // Delete an employee notification
    public void deleteEmployeeNotification(Integer id) {
        logger.debug("Deleting EmployeeNotification with ID: {}", id);
        EmployeeNotification existingNotification = employeeNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeNotification not found with ID: " + id));
        employeeNotificationRepository.delete(existingNotification);
        logger.debug("Deleted EmployeeNotification with ID: {}", id);
    }
}
