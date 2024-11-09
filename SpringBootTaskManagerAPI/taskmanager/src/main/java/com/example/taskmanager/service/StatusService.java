package com.example.taskmanager.service;

import com.example.taskmanager.dto.StatusRequestDTO;
import com.example.taskmanager.dto.StatusResponseDTO;
import com.example.taskmanager.entity.Status;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.StatusMapper;
import com.example.taskmanager.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatusService {
    
    private final StatusRepository statusRepository;
    private final StatusMapper statusMapper;

    public StatusService(StatusRepository statusRepository, StatusMapper statusMapper) {
        this.statusRepository = statusRepository;
        this.statusMapper = statusMapper;
    }

    public List<StatusResponseDTO> getAllStatuses() {
        return statusRepository.findAll().stream()
                               .map(statusMapper::toDTO)
                               .collect(Collectors.toList());
    }

    public StatusResponseDTO getStatusById(Integer id) {
        Status status = statusRepository.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("Status not found with ID: " + id));
        return statusMapper.toDTO(status);
    }

    public StatusResponseDTO createStatus(StatusRequestDTO statusRequestDTO) {
        Status status = statusMapper.toEntity(statusRequestDTO);
        status = statusRepository.save(status);
        return statusMapper.toDTO(status);
    }

    public StatusResponseDTO updateStatus(Integer id, StatusRequestDTO statusRequestDTO) {
        Status existingStatus = statusRepository.findById(id)
                                                .orElseThrow(() -> new ResourceNotFoundException("Status not found with ID: " + id));
        existingStatus.setStatusName(statusRequestDTO.getStatusName());
        Status updatedStatus = statusRepository.save(existingStatus);
        return statusMapper.toDTO(updatedStatus);
    }

    public void deleteStatus(Integer id) {
        Status status = statusRepository.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("Status not found with ID: " + id));
        statusRepository.delete(status);
    }
}
