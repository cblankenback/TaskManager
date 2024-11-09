package com.example.taskmanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.dto.NotificationRequestDTO;
import com.example.taskmanager.dto.NotificationResponseDTO;
import com.example.taskmanager.service.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
	
	private final NotificationService notificationService;
	
	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}
	
	//Create a new notifications

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createTask(@Valid @RequestBody NotificationRequestDTO notificationRequestDTO) {
    	NotificationResponseDTO createdNotification = notificationService.createNotification(notificationRequestDTO);
        return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
    }
 // Retrieve all notifications
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAllTasks() {
        List<NotificationResponseDTO> notifications = notificationService.getAllNotification();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }
 // Notification task by ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable Integer id) {
    	NotificationResponseDTO notification = notificationService.getNotificationById(id);
        return new ResponseEntity<>(notification, HttpStatus.OK);
    }
 // Update an existing task
    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> updateNotification(@PathVariable Integer id, @Valid @RequestBody NotificationRequestDTO notificationRequestDTO) {
    	NotificationResponseDTO updatedTask = notificationService.updateNotification(id, notificationRequestDTO);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }
    // Delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Integer id) {
        notificationService.deleteNotification(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
