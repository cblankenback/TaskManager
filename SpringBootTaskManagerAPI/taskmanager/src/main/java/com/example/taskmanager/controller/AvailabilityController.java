package com.example.taskmanager.controller;

import com.example.taskmanager.dto.AvailabilityRequestDTO;
import com.example.taskmanager.dto.AvailabilityResponseDTO;
import com.example.taskmanager.service.AvailabilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
public class AvailabilityController {
    
    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping
    public ResponseEntity<List<AvailabilityResponseDTO>> getAllAvailabilities() {
        List<AvailabilityResponseDTO> availabilities = availabilityService.getAllAvailabilities();
        return ResponseEntity.ok(availabilities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDTO> getAvailabilityById(@PathVariable Integer id) {
        AvailabilityResponseDTO availability = availabilityService.getAvailabilityById(id);
        return ResponseEntity.ok(availability);
    }

    @PostMapping
    public ResponseEntity<AvailabilityResponseDTO> createAvailability(@Validated @RequestBody AvailabilityRequestDTO availabilityRequestDTO) {
        AvailabilityResponseDTO createdAvailability = availabilityService.createAvailability(availabilityRequestDTO);
        return new ResponseEntity<>(createdAvailability, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDTO> updateAvailability(@PathVariable Integer id,
                                                                      @Validated @RequestBody AvailabilityRequestDTO availabilityRequestDTO) {
        AvailabilityResponseDTO updatedAvailability = availabilityService.updateAvailability(id, availabilityRequestDTO);
        return ResponseEntity.ok(updatedAvailability);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Integer id) {
        availabilityService.deleteAvailability(id);
        return ResponseEntity.noContent().build();
    }
}
