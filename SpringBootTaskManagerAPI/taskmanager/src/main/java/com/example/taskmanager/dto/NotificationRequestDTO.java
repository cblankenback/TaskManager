package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NotificationRequestDTO {

	@NotNull(message = "Task ID is required")
	private Integer taskId;
	@NotBlank(message = "Message is required")
	@Size(max = 500, message = "Message must be at most 500 characters")
	private String message;
}
