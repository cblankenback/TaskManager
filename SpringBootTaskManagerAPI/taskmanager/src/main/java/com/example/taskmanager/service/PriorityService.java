package com.example.taskmanager.service;

import com.example.taskmanager.dto.PriorityRequestDTO;
import com.example.taskmanager.dto.PriorityResponseDTO;
import com.example.taskmanager.entity.Priority;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.PriorityMapper;
import com.example.taskmanager.repository.PriorityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriorityService {
    
    private final PriorityRepository priorityRepository;
    private final PriorityMapper priorityMapper;

    public PriorityService(PriorityRepository priorityRepository, PriorityMapper priorityMapper) {
        this.priorityRepository = priorityRepository;
        this.priorityMapper = priorityMapper;
    }

    public List<PriorityResponseDTO> getAllPriorities() {
        return priorityRepository.findAll().stream()
                                 .map(priorityMapper::toDTO)
                                 .collect(Collectors.toList());
    }

    public PriorityResponseDTO getPriorityById(Integer id) {
        Priority priority = priorityRepository.findById(id)
                                              .orElseThrow(() -> new ResourceNotFoundException("Priority not found with ID: " + id));
        return priorityMapper.toDTO(priority);
    }

    public PriorityResponseDTO createPriority(PriorityRequestDTO priorityRequestDTO) {
        Priority priority = priorityMapper.toEntity(priorityRequestDTO);
        priority = priorityRepository.save(priority);
        return priorityMapper.toDTO(priority);
    }

    public PriorityResponseDTO updatePriority(Integer id, PriorityRequestDTO priorityRequestDTO) {
        Priority existingPriority = priorityRepository.findById(id)
                                                      .orElseThrow(() -> new ResourceNotFoundException("Priority not found with ID: " + id));
        existingPriority.setType(priorityRequestDTO.getType());
        Priority updatedPriority = priorityRepository.save(existingPriority);
        return priorityMapper.toDTO(updatedPriority);
    }

    public void deletePriority(Integer id) {
        Priority priority = priorityRepository.findById(id)
                                              .orElseThrow(() -> new ResourceNotFoundException("Priority not found with ID: " + id));
        priorityRepository.delete(priority);
    }
}
