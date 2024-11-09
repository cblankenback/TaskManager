package com.example.taskmanager;

import com.example.taskmanager.dto.DepartmentRequestDTO;
import com.example.taskmanager.dto.DepartmentResponseDTO;
import com.example.taskmanager.entity.Department;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.DepartmentMapper;
import com.example.taskmanager.repository.DepartmentRepository;
import com.example.taskmanager.service.DepartmentService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void testGetAllDepartments() {
        // Given
        Department department1 = new Department();
        department1.setDepartmentId(1);
        department1.setDepartmentName("HR");

        Department department2 = new Department();
        department2.setDepartmentId(2);
        department2.setDepartmentName("IT");

        List<Department> departments = Arrays.asList(department1, department2);

        DepartmentResponseDTO dto1 = new DepartmentResponseDTO();
        dto1.setDepartmentId(1);
        dto1.setDepartmentName("HR");

        DepartmentResponseDTO dto2 = new DepartmentResponseDTO();
        dto2.setDepartmentId(2);
        dto2.setDepartmentName("IT");

        when(departmentRepository.findAll()).thenReturn(departments);
        when(departmentMapper.toDTO(department1)).thenReturn(dto1);
        when(departmentMapper.toDTO(department2)).thenReturn(dto2);

        // When
        List<DepartmentResponseDTO> result = departmentService.getAllDepartments();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("HR", result.get(0).getDepartmentName());
        assertEquals("IT", result.get(1).getDepartmentName());

        verify(departmentRepository, times(1)).findAll();
        verify(departmentMapper, times(1)).toDTO(department1);
        verify(departmentMapper, times(1)).toDTO(department2);
    }

    @Test
    void testGetDepartmentById_Success() {
        // Given
        Department department = new Department();
        department.setDepartmentId(1);
        department.setDepartmentName("HR");

        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setDepartmentId(1);
        dto.setDepartmentName("HR");

        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(departmentMapper.toDTO(department)).thenReturn(dto);

        // When
        DepartmentResponseDTO result = departmentService.getDepartmentById(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getDepartmentId());
        assertEquals("HR", result.getDepartmentName());

        verify(departmentRepository, times(1)).findById(1);
        verify(departmentMapper, times(1)).toDTO(department);
    }

    @Test
    void testGetDepartmentById_NotFound() {
        // Given
        when(departmentRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            departmentService.getDepartmentById(1);
        });
        assertEquals("Department not found with ID: 1", exception.getMessage());
        verify(departmentRepository, times(1)).findById(1);
        verify(departmentMapper, never()).toDTO(any());
    }

    @Test
    void testCreateDepartment() {
        // Given
        DepartmentRequestDTO requestDTO = new DepartmentRequestDTO();
        requestDTO.setDepartmentName("Finance");

        Department departmentEntity = new Department();
        departmentEntity.setDepartmentName("Finance");

        Department savedDepartment = new Department();
        savedDepartment.setDepartmentId(1);
        savedDepartment.setDepartmentName("Finance");

        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO();
        responseDTO.setDepartmentId(1);
        responseDTO.setDepartmentName("Finance");

        when(departmentMapper.toEntity(requestDTO)).thenReturn(departmentEntity);
        when(departmentRepository.save(departmentEntity)).thenReturn(savedDepartment);
        when(departmentMapper.toDTO(savedDepartment)).thenReturn(responseDTO);

        // When
        DepartmentResponseDTO result = departmentService.createDepartment(requestDTO);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getDepartmentId());
        assertEquals("Finance", result.getDepartmentName());
        verify(departmentMapper, times(1)).toEntity(requestDTO);
        verify(departmentRepository, times(1)).save(departmentEntity);
        verify(departmentMapper, times(1)).toDTO(savedDepartment);
    }

    @Test
    void testUpdateDepartment_Success() {
        // Given
        DepartmentRequestDTO requestDTO = new DepartmentRequestDTO();
        requestDTO.setDepartmentName("Operations");

        Department existingDepartment = new Department();
        existingDepartment.setDepartmentId(1);
        existingDepartment.setDepartmentName("HR");

        Department updatedDepartment = new Department();
        updatedDepartment.setDepartmentId(1);
        updatedDepartment.setDepartmentName("Operations");

        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO();
        responseDTO.setDepartmentId(1);
        responseDTO.setDepartmentName("Operations");

        when(departmentRepository.findById(1)).thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.save(existingDepartment)).thenReturn(updatedDepartment);
        when(departmentMapper.toDTO(updatedDepartment)).thenReturn(responseDTO);

        // When
        DepartmentResponseDTO result = departmentService.updateDepartment(1, requestDTO);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getDepartmentId());
        assertEquals("Operations", result.getDepartmentName());

        verify(departmentRepository, times(1)).findById(1);
        verify(departmentRepository, times(1)).save(existingDepartment);
        verify(departmentMapper, times(1)).toDTO(updatedDepartment);
    }

    @Test
    void testUpdateDepartment_NotFound() {
        // Given
        DepartmentRequestDTO requestDTO = new DepartmentRequestDTO();
        requestDTO.setDepartmentName("Operations");

        when(departmentRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            departmentService.updateDepartment(1, requestDTO);
        });
        assertEquals("Department not found with ID: 1", exception.getMessage());
        verify(departmentRepository, times(1)).findById(1);
        verify(departmentRepository, never()).save(any());
        verify(departmentMapper, never()).toDTO(any());
    }

    @Test
    void testDeleteDepartment_Success() {
        // Given
        Department existingDepartment = new Department();
        existingDepartment.setDepartmentId(1);
        existingDepartment.setDepartmentName("HR");

        when(departmentRepository.findById(1)).thenReturn(Optional.of(existingDepartment));
        doNothing().when(departmentRepository).delete(existingDepartment);

        // When
        departmentService.deleteDepartment(1);

        // Then
        verify(departmentRepository, times(1)).findById(1);
        verify(departmentRepository, times(1)).delete(existingDepartment);
    }

    @Test
    void testDeleteDepartment_NotFound() {
        // Given
        when(departmentRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            departmentService.deleteDepartment(1);
        });
        assertEquals("Department not found with ID: 1", exception.getMessage());
        verify(departmentRepository, times(1)).findById(1);
        verify(departmentRepository, never()).delete(any());
    }
}
