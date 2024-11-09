package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskUpdateRequestDTO;
import com.example.taskmanager.dto.TaskUpdateResponseDTO;
import com.example.taskmanager.service.TaskUpdateService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taskupdates")
public class TaskUpdateController {

    private final TaskUpdateService taskUpdateService;

    public TaskUpdateController(TaskUpdateService taskUpdateService) {
        this.taskUpdateService = taskUpdateService;
    }

    // Create a new task update
    @PostMapping
    public ResponseEntity<TaskUpdateResponseDTO> createTaskUpdate(@Valid @RequestBody TaskUpdateRequestDTO requestDTO) {
        TaskUpdateResponseDTO createdTaskUpdate = taskUpdateService.createTaskUpdate(requestDTO);
        return new ResponseEntity<>(createdTaskUpdate, HttpStatus.CREATED);
    }

    // Retrieve all task updates
    @GetMapping
    public ResponseEntity<List<TaskUpdateResponseDTO>> getAllTaskUpdates() {
        List<TaskUpdateResponseDTO> taskUpdates = taskUpdateService.getAllTaskUpdates();
        return new ResponseEntity<>(taskUpdates, HttpStatus.OK);
    }

    // Retrieve task update by ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskUpdateResponseDTO> getTaskUpdateById(@PathVariable Integer id) {
        TaskUpdateResponseDTO taskUpdate = taskUpdateService.getTaskUpdateById(id);
        return new ResponseEntity<>(taskUpdate, HttpStatus.OK);
    }

    // Update an existing task update
    @PutMapping("/{id}")
    public ResponseEntity<TaskUpdateResponseDTO> updateTaskUpdate(@PathVariable Integer id,
                                                                    @Valid @RequestBody TaskUpdateRequestDTO requestDTO) {
        TaskUpdateResponseDTO updatedTaskUpdate = taskUpdateService.updateTaskUpdate(id, requestDTO);
        return new ResponseEntity<>(updatedTaskUpdate, HttpStatus.OK);
    }

    // Delete a task update
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskUpdate(@PathVariable Integer id) {
        taskUpdateService.deleteTaskUpdate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
