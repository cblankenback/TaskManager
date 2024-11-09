package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AvailabilityRequestDTO {
	 @NotBlank(message = "Message is required")
	 @Size(max = 45, message = "Message must be at most 45 characters")
	 private String availabilityName;
}
