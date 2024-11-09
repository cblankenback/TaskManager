package com.example.taskmanager.controller;

import com.example.taskmanager.dto.EmployeeNotificationRequestDTO;
import com.example.taskmanager.dto.EmployeeNotificationResponseDTO;
import com.example.taskmanager.service.EmployeeNotificationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employeeNotifications")
public class EmployeeNotificationController {

    private final EmployeeNotificationService employeeNotificationService;

    public EmployeeNotificationController(EmployeeNotificationService employeeNotificationService) {
        this.employeeNotificationService = employeeNotificationService;
    }

    // Create a new employee notification
    @PostMapping
    public ResponseEntity<EmployeeNotificationResponseDTO> createEmployeeNotification(@Valid @RequestBody EmployeeNotificationRequestDTO requestDTO) {
        EmployeeNotificationResponseDTO createdNotification = employeeNotificationService.createEmployeeNotification(requestDTO);
        return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
    }

    // Retrieve all employee notifications
    @GetMapping
    public ResponseEntity<List<EmployeeNotificationResponseDTO>> getAllEmployeeNotifications() {
        List<EmployeeNotificationResponseDTO> notifications = employeeNotificationService.getAllEmployeeNotifications();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    // Retrieve employee notification by ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeNotificationResponseDTO> getEmployeeNotificationById(@PathVariable Integer id) {
        EmployeeNotificationResponseDTO notification = employeeNotificationService.getEmployeeNotificationById(id);
        return new ResponseEntity<>(notification, HttpStatus.OK);
    }

    // Update an existing employee notification
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeNotificationResponseDTO> updateEmployeeNotification(@PathVariable Integer id, @Valid @RequestBody EmployeeNotificationRequestDTO requestDTO) {
        EmployeeNotificationResponseDTO updatedNotification = employeeNotificationService.updateEmployeeNotification(id, requestDTO);
        return new ResponseEntity<>(updatedNotification, HttpStatus.OK);
    }

    // Delete an employee notification
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeNotification(@PathVariable Integer id) {
        employeeNotificationService.deleteEmployeeNotification(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
