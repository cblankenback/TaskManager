package com.example.taskmanager;

import com.example.taskmanager.dto.AvailabilityRequestDTO;
import com.example.taskmanager.dto.AvailabilityResponseDTO;
import com.example.taskmanager.entity.Availability;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.AvailabilityMapper;
import com.example.taskmanager.repository.AvailabilityRepository;
import com.example.taskmanager.service.AvailabilityService;

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
public class AvailabilityServiceTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private AvailabilityMapper availabilityMapper;

    @InjectMocks
    private AvailabilityService availabilityService;

    @Test
    void testGetAllAvailabilities() {
        // Given
        Availability availability1 = new Availability();
        availability1.setAvailabilityId(1);
        availability1.setAvailabilityName("Available");

        Availability availability2 = new Availability();
        availability2.setAvailabilityId(2);
        availability2.setAvailabilityName("Unavailable");

        List<Availability> availabilities = Arrays.asList(availability1, availability2);

        AvailabilityResponseDTO dto1 = new AvailabilityResponseDTO();
        dto1.setAvailabilityNameId(1);
        dto1.setAvailabilityNameName("Available");

        AvailabilityResponseDTO dto2 = new AvailabilityResponseDTO();
        dto2.setAvailabilityNameId(2);
        dto2.setAvailabilityNameName("Unavailable");

        when(availabilityRepository.findAll()).thenReturn(availabilities);
        when(availabilityMapper.toDTO(availability1)).thenReturn(dto1);
        when(availabilityMapper.toDTO(availability2)).thenReturn(dto2);

        // When
        List<AvailabilityResponseDTO> result = availabilityService.getAllAvailabilities();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getAvailabilityNameId());
        assertEquals("Available", result.get(0).getAvailabilityNameName());
        assertEquals(2, result.get(1).getAvailabilityNameId());
        assertEquals("Unavailable", result.get(1).getAvailabilityNameName());

        verify(availabilityRepository, times(1)).findAll();
        verify(availabilityMapper, times(1)).toDTO(availability1);
        verify(availabilityMapper, times(1)).toDTO(availability2);
    }

    @Test
    void testGetAvailabilityById_Success() {
        // Given
        Availability availability = new Availability();
        availability.setAvailabilityId(1);
        availability.setAvailabilityName("Available");

        AvailabilityResponseDTO dto = new AvailabilityResponseDTO();
        dto.setAvailabilityNameId(1);
        dto.setAvailabilityNameName("Available");

        when(availabilityRepository.findById(1)).thenReturn(Optional.of(availability));
        when(availabilityMapper.toDTO(availability)).thenReturn(dto);

        // When
        AvailabilityResponseDTO result = availabilityService.getAvailabilityById(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getAvailabilityNameId());
        assertEquals("Available", result.getAvailabilityNameName());
        verify(availabilityRepository, times(1)).findById(1);
        verify(availabilityMapper, times(1)).toDTO(availability);
    }

    @Test
    void testGetAvailabilityById_NotFound() {
        // Given
        when(availabilityRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            availabilityService.getAvailabilityById(1);
        });
        assertEquals("Availability not found with ID: 1", exception.getMessage());
        verify(availabilityRepository, times(1)).findById(1);
        verify(availabilityMapper, never()).toDTO(any());
    }

    @Test
    void testCreateAvailability() {
        // Given
        AvailabilityRequestDTO requestDTO = new AvailabilityRequestDTO();
        requestDTO.setAvailabilityName("Available");

        Availability availability = new Availability();
        availability.setAvailabilityName("Available");

        Availability savedAvailability = new Availability();
        savedAvailability.setAvailabilityId(1);
        savedAvailability.setAvailabilityName("Available");

        AvailabilityResponseDTO responseDTO = new AvailabilityResponseDTO();
        responseDTO.setAvailabilityNameId(1);
        responseDTO.setAvailabilityNameName("Available");

        when(availabilityMapper.toEntity(requestDTO)).thenReturn(availability);
        when(availabilityRepository.save(availability)).thenReturn(savedAvailability);
        when(availabilityMapper.toDTO(savedAvailability)).thenReturn(responseDTO);

        // When
        AvailabilityResponseDTO result = availabilityService.createAvailability(requestDTO);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getAvailabilityNameId());
        assertEquals("Available", result.getAvailabilityNameName());
        verify(availabilityMapper, times(1)).toEntity(requestDTO);
        verify(availabilityRepository, times(1)).save(availability);
        verify(availabilityMapper, times(1)).toDTO(savedAvailability);
    }

    @Test
    void testUpdateAvailability_Success() {
        // Given
        AvailabilityRequestDTO requestDTO = new AvailabilityRequestDTO();
        requestDTO.setAvailabilityName("Unavailable");

        Availability existingAvailability = new Availability();
        existingAvailability.setAvailabilityId(1);
        existingAvailability.setAvailabilityName("Available");

        Availability updatedAvailability = new Availability();
        updatedAvailability.setAvailabilityId(1);
        updatedAvailability.setAvailabilityName("Unavailable");

        AvailabilityResponseDTO responseDTO = new AvailabilityResponseDTO();
        responseDTO.setAvailabilityNameId(1);
        responseDTO.setAvailabilityNameName("Unavailable");

        when(availabilityRepository.findById(1)).thenReturn(Optional.of(existingAvailability));
        when(availabilityRepository.save(existingAvailability)).thenReturn(updatedAvailability);
        when(availabilityMapper.toDTO(updatedAvailability)).thenReturn(responseDTO);

        // When
        AvailabilityResponseDTO result = availabilityService.updateAvailability(1, requestDTO);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getAvailabilityNameId());
        assertEquals("Unavailable", result.getAvailabilityNameName());
        verify(availabilityRepository, times(1)).findById(1);
        verify(availabilityRepository, times(1)).save(existingAvailability);
        verify(availabilityMapper, times(1)).toDTO(updatedAvailability);
    }

    @Test
    void testUpdateAvailability_NotFound() {
        // Given
        AvailabilityRequestDTO requestDTO = new AvailabilityRequestDTO();
        requestDTO.setAvailabilityName("Unavailable");

        when(availabilityRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            availabilityService.updateAvailability(1, requestDTO);
        });
        assertEquals("Availability not found with ID: 1", exception.getMessage());
        verify(availabilityRepository, times(1)).findById(1);
        verify(availabilityRepository, never()).save(any());
        verify(availabilityMapper, never()).toDTO(any());
    }

    @Test
    void testDeleteAvailability_Success() {
        // Given
        Availability existingAvailability = new Availability();
        existingAvailability.setAvailabilityId(1);
        existingAvailability.setAvailabilityName("Available");

        when(availabilityRepository.findById(1)).thenReturn(Optional.of(existingAvailability));
        doNothing().when(availabilityRepository).delete(existingAvailability);

        // When
        availabilityService.deleteAvailability(1);

        // Then
        verify(availabilityRepository, times(1)).findById(1);
        verify(availabilityRepository, times(1)).delete(existingAvailability);
    }

    @Test
    void testDeleteAvailability_NotFound() {
        // Given
        when(availabilityRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            availabilityService.deleteAvailability(1);
        });
        assertEquals("Availability not found with ID: 1", exception.getMessage());
        verify(availabilityRepository, times(1)).findById(1);
        verify(availabilityRepository, never()).delete(any());
    }
}
