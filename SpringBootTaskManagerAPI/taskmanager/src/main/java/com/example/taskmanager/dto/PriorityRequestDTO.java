package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PriorityRequestDTO {
    @NotBlank(message = "Type is required")
    @Size(max = 45, message = "Type must be at most 45 characters")
    private String type;
}
