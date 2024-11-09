package com.example.taskmanager.service;

import com.example.taskmanager.dto.DepartmentRequestDTO;
import com.example.taskmanager.dto.DepartmentResponseDTO;
import com.example.taskmanager.entity.Department;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.DepartmentMapper;
import com.example.taskmanager.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                                   .map(departmentMapper::toDTO)
                                   .collect(Collectors.toList());
    }

    public DepartmentResponseDTO getDepartmentById(Integer id) {
        Department department = departmentRepository.findById(id)
                                                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        return departmentMapper.toDTO(department);
    }

    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequestDTO) {
        Department department = departmentMapper.toEntity(departmentRequestDTO);
        department = departmentRepository.save(department);
        return departmentMapper.toDTO(department);
    }

    public DepartmentResponseDTO updateDepartment(Integer id, DepartmentRequestDTO departmentRequestDTO) {
        Department existingDepartment = departmentRepository.findById(id)
                                                            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        existingDepartment.setDepartmentName(departmentRequestDTO.getDepartmentName());
        Department updatedDepartment = departmentRepository.save(existingDepartment);
        return departmentMapper.toDTO(updatedDepartment);
    }

    public void deleteDepartment(Integer id) {
        Department department = departmentRepository.findById(id)
                                                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        departmentRepository.delete(department);
    }
}
