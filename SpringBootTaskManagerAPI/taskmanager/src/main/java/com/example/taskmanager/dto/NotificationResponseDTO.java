package com.example.taskmanager.dto;

import lombok.Data;

@Data
public class NotificationResponseDTO {
	private Integer notificationId;
	private Integer taskId;
	private String message;
}
