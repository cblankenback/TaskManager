package com.example.taskmanager.controller;

import com.example.taskmanager.dto.StatusRequestDTO;
import com.example.taskmanager.dto.StatusResponseDTO;
import com.example.taskmanager.service.StatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statuses")
public class StatusController {
    
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public ResponseEntity<List<StatusResponseDTO>> getAllStatuses() {
        List<StatusResponseDTO> statuses = statusService.getAllStatuses();
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusResponseDTO> getStatusById(@PathVariable Integer id) {
        StatusResponseDTO status = statusService.getStatusById(id);
        return ResponseEntity.ok(status);
    }

    @PostMapping
    public ResponseEntity<StatusResponseDTO> createStatus(@Validated @RequestBody StatusRequestDTO statusRequestDTO) {
        StatusResponseDTO createdStatus = statusService.createStatus(statusRequestDTO);
        return new ResponseEntity<>(createdStatus, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusResponseDTO> updateStatus(@PathVariable Integer id,
                                                          @Validated @RequestBody StatusRequestDTO statusRequestDTO) {
        StatusResponseDTO updatedStatus = statusService.updateStatus(id, statusRequestDTO);
        return ResponseEntity.ok(updatedStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Integer id) {
        statusService.deleteStatus(id);
        return ResponseEntity.noContent().build();
    }
}
