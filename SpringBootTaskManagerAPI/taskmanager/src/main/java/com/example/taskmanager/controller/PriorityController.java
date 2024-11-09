package com.example.taskmanager.controller;

import com.example.taskmanager.dto.PriorityRequestDTO;
import com.example.taskmanager.dto.PriorityResponseDTO;
import com.example.taskmanager.service.PriorityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/priorities")
public class PriorityController {
    
    private final PriorityService priorityService;

    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @GetMapping
    public ResponseEntity<List<PriorityResponseDTO>> getAllPriorities() {
        List<PriorityResponseDTO> priorities = priorityService.getAllPriorities();
        return ResponseEntity.ok(priorities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriorityResponseDTO> getPriorityById(@PathVariable Integer id) {
        PriorityResponseDTO priority = priorityService.getPriorityById(id);
        return ResponseEntity.ok(priority);
    }

    @PostMapping
    public ResponseEntity<PriorityResponseDTO> createPriority(@Validated @RequestBody PriorityRequestDTO priorityRequestDTO) {
        PriorityResponseDTO createdPriority = priorityService.createPriority(priorityRequestDTO);
        return new ResponseEntity<>(createdPriority, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriorityResponseDTO> updatePriority(@PathVariable Integer id,
                                                              @Validated @RequestBody PriorityRequestDTO priorityRequestDTO) {
        PriorityResponseDTO updatedPriority = priorityService.updatePriority(id, priorityRequestDTO);
        return ResponseEntity.ok(updatedPriority);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriority(@PathVariable Integer id) {
        priorityService.deletePriority(id);
        return ResponseEntity.noContent().build();
    }
}
