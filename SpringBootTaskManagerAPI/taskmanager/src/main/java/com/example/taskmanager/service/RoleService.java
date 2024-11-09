package com.example.taskmanager.service;

import com.example.taskmanager.dto.RoleRequestDTO;
import com.example.taskmanager.dto.RoleResponseDTO;
import com.example.taskmanager.entity.Role;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.RoleMapper;
import com.example.taskmanager.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                             .map(roleMapper::toDTO)
                             .collect(Collectors.toList());
    }

    public RoleResponseDTO getRoleById(Integer id) {
        Role role = roleRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));
        return roleMapper.toDTO(role);
    }

    public RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO) {
        Role role = roleMapper.toEntity(roleRequestDTO);
        role = roleRepository.save(role);
        return roleMapper.toDTO(role);
    }

    public RoleResponseDTO updateRole(Integer id, RoleRequestDTO roleRequestDTO) {
        Role existingRole = roleRepository.findById(id)
                                          .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));
        existingRole.setRoleName(roleRequestDTO.getRoleName());
        Role updatedRole = roleRepository.save(existingRole);
        return roleMapper.toDTO(updatedRole);
    }

    public void deleteRole(Integer id) {
        Role role = roleRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));
        roleRepository.delete(role);
    }
}
