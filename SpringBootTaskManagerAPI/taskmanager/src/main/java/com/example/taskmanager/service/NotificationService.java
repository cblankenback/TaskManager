package com.example.taskmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


import com.example.taskmanager.dto.NotificationRequestDTO;
import com.example.taskmanager.dto.NotificationResponseDTO;

import com.example.taskmanager.entity.Notification;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.NotificationMapper;
import com.example.taskmanager.repository.NotificationRepository;
import com.example.taskmanager.repository.TaskRepository;

@Service
public class NotificationService {

		private final NotificationRepository notificationRepository;
		private final NotificationMapper notificationMapper;
		private final TaskRepository taskRepository;
		
		public NotificationService(NotificationRepository notificationRepository,
									NotificationMapper notificationMapper,
									TaskRepository taskRepository) {
			
			this.notificationRepository = notificationRepository;
	        this.notificationMapper = notificationMapper;
	        this.taskRepository = taskRepository;
		}
		
		 // Retrieve all notification
	    public List<NotificationResponseDTO> getAllNotification() {
	        List<Notification> notification = notificationRepository.findAll();
	        return notification.stream()
	                       .map(notificationMapper::toDTO)
	                       .collect(Collectors.toList());
	    }

	    // Retrieve a notification by ID
	    public NotificationResponseDTO getNotificationById(Integer id) {
	    	Notification notification = notificationRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));
	        return notificationMapper.toDTO(notification);
	    }
	    
	    public NotificationResponseDTO createNotification(NotificationRequestDTO notificationRequestDTO) {
			
	    	
	    	Notification notification = notificationMapper.toEntity(notificationRequestDTO);
	    	
	    	Integer taskId = notificationRequestDTO.getTaskId();
	    	Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + taskId));
	    	notification.setTask(task);
	    	
	    	
	    	Notification savedComment = notificationRepository.save(notification);
	    	
	        return notificationMapper.toDTO(savedComment);
	    	
	    }

	 // Update an existing notification
	    public NotificationResponseDTO updateNotification(Integer id, NotificationRequestDTO notificationRequestDTO) {
	    	Notification existingNotification = notificationRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));
	        
	        existingNotification.setMessage(notificationRequestDTO.getMessage());
	        
	        Integer taskId = notificationRequestDTO.getTaskId();
	        Task task = taskRepository.findById(taskId) .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));
	        existingNotification.setTask(task);
	        
	        
	        // Update other fields if there are any

	        Notification updatedNotification = notificationRepository.save(existingNotification);
	        return notificationMapper.toDTO(updatedNotification);
	    }
	    
	    public void deleteNotification(Integer id) {
	    	Notification existingNotification = notificationRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id)); 
	    	notificationRepository.delete(existingNotification);
	    	
	    }
}
