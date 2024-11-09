package com.example.taskmanager.service;

import com.example.taskmanager.dto.AvailabilityRequestDTO;
import com.example.taskmanager.dto.AvailabilityResponseDTO;
import com.example.taskmanager.entity.Availability;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.AvailabilityMapper;
import com.example.taskmanager.repository.AvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {
    
    private final AvailabilityRepository availabilityRepository;
    private final AvailabilityMapper availabilityMapper;

    public AvailabilityService(AvailabilityRepository availabilityRepository, AvailabilityMapper availabilityMapper) {
        this.availabilityRepository = availabilityRepository;
        this.availabilityMapper = availabilityMapper;
    }

    public List<AvailabilityResponseDTO> getAllAvailabilities() {
        return availabilityRepository.findAll().stream()
                                     .map(availabilityMapper::toDTO)
                                     .collect(Collectors.toList());
    }

    public AvailabilityResponseDTO getAvailabilityById(Integer id) {
        Availability availability = availabilityRepository.findById(id)
                                                          .orElseThrow(() -> new ResourceNotFoundException("Availability not found with ID: " + id));
        return availabilityMapper.toDTO(availability);
    }

    public AvailabilityResponseDTO createAvailability(AvailabilityRequestDTO availabilityRequestDTO) {
        Availability availability = availabilityMapper.toEntity(availabilityRequestDTO);
        availability = availabilityRepository.save(availability);
        return availabilityMapper.toDTO(availability);
    }

    public AvailabilityResponseDTO updateAvailability(Integer id, AvailabilityRequestDTO availabilityRequestDTO) {
        Availability existingAvailability = availabilityRepository.findById(id)
                                                                  .orElseThrow(() -> new ResourceNotFoundException("Availability not found with ID: " + id));
        existingAvailability.setAvailabilityName(availabilityRequestDTO.getAvailabilityName());
        Availability updatedAvailability = availabilityRepository.save(existingAvailability);
        return availabilityMapper.toDTO(updatedAvailability);
    }

    public void deleteAvailability(Integer id) {
        Availability availability = availabilityRepository.findById(id)
                                                          .orElseThrow(() -> new ResourceNotFoundException("Availability not found with ID: " + id));
        availabilityRepository.delete(availability);
    }
}
