package com.example.taskmanager;

import com.example.taskmanager.dto.EmployeeRequestDTO;
import com.example.taskmanager.dto.EmployeeResponseDTO;
import com.example.taskmanager.entity.Availability;
import com.example.taskmanager.entity.Department;
import com.example.taskmanager.entity.Employee;
import com.example.taskmanager.entity.Role;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.EmployeeMapper;
import com.example.taskmanager.repository.AvailabilityRepository;
import com.example.taskmanager.repository.DepartmentRepository;
import com.example.taskmanager.repository.EmployeeRepository;
import com.example.taskmanager.repository.RoleRepository;
import com.example.taskmanager.service.EmployeeService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void testCreateEmployee_Success() {
        // Given
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setPassword("password");
        requestDTO.setDepartmentId(1);
        requestDTO.setAvailabilityId(1);
        requestDTO.setRoleId(1);

        Employee employeeEntity = new Employee();
        employeeEntity.setFirstName("John");
        employeeEntity.setLastName("Doe");
        // Password will be set after encoding

        Department department = new Department();
        department.setDepartmentId(1);
        department.setDepartmentName("IT");

        Availability availability = new Availability();
        availability.setAvailabilityId(1);
        availability.setAvailabilityName("Available");

        Role role = new Role();
        role.setRoleId(1);
        role.setRoleName("Admin");

        Employee savedEmployee = new Employee();
        savedEmployee.setEmployeeId(1);
        savedEmployee.setFirstName("John");
        savedEmployee.setLastName("Doe");
        savedEmployee.setPassword("encodedPassword");
        savedEmployee.setDepartment(department);
        savedEmployee.setAvailability(availability);
        savedEmployee.setRole(role);

        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO();
        responseDTO.setEmployeeId(1);
        responseDTO.setFirstName("John");
        responseDTO.setLastName("Doe");
        responseDTO.setDepartmentId(1);
        responseDTO.setAvailabilityId(1);
        responseDTO.setRoleId(1);

        when(employeeMapper.toEntity(requestDTO)).thenReturn(employeeEntity);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(availabilityRepository.findById(1)).thenReturn(Optional.of(availability));
        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
        when(employeeRepository.save(employeeEntity)).thenReturn(savedEmployee);
        when(employeeMapper.toDTO(savedEmployee)).thenReturn(responseDTO);

        // When
        EmployeeResponseDTO result = employeeService.createEmployee(requestDTO);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getEmployeeId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(1, result.getDepartmentId());
        assertEquals(1, result.getAvailabilityId());
        assertEquals(1, result.getRoleId());

        verify(employeeMapper, times(1)).toEntity(requestDTO);
        verify(passwordEncoder, times(1)).encode("password");
        verify(departmentRepository, times(1)).findById(1);
        verify(availabilityRepository, times(1)).findById(1);
        verify(roleRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).save(employeeEntity);
        verify(employeeMapper, times(1)).toDTO(savedEmployee);
    }

    @Test
    void testCreateEmployee_MissingPassword() {
        // Given
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setFirstName("Jane");
        requestDTO.setLastName("Smith");
        // Password is missing
        requestDTO.setDepartmentId(2);
        requestDTO.setAvailabilityId(2);
        requestDTO.setRoleId(2);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.createEmployee(requestDTO);
        });

        assertEquals("Password is required when creating an employee", exception.getMessage());

        verify(employeeMapper, never()).toEntity(any());
        verify(passwordEncoder, never()).encode(anyString());
        verify(departmentRepository, never()).findById(anyInt());
        verify(availabilityRepository, never()).findById(anyInt());
        verify(roleRepository, never()).findById(anyInt());
        verify(employeeRepository, never()).save(any());
        verify(employeeMapper, never()).toDTO(any());
    }

    @Test
    void testGetAllEmployees() {
        // Given
        Employee employee1 = new Employee();
        employee1.setEmployeeId(1);
        employee1.setFirstName("John");
        employee1.setLastName("Doe");

        Employee employee2 = new Employee();
        employee2.setEmployeeId(2);
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");

        List<Employee> employees = Arrays.asList(employee1, employee2);

        EmployeeResponseDTO dto1 = new EmployeeResponseDTO();
        dto1.setEmployeeId(1);
        dto1.setFirstName("John");
        dto1.setLastName("Doe");
        dto1.setDepartmentId(1);
        dto1.setAvailabilityId(1);
        dto1.setRoleId(1);

        EmployeeResponseDTO dto2 = new EmployeeResponseDTO();
        dto2.setEmployeeId(2);
        dto2.setFirstName("Jane");
        dto2.setLastName("Smith");
        dto2.setDepartmentId(2);
        dto2.setAvailabilityId(2);
        dto2.setRoleId(2);

        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.toDTO(employee1)).thenReturn(dto1);
        when(employeeMapper.toDTO(employee2)).thenReturn(dto2);

        // When
        List<EmployeeResponseDTO> result = employeeService.getAllEmployees();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());

        verify(employeeRepository, times(1)).findAll();
        verify(employeeMapper, times(1)).toDTO(employee1);
        verify(employeeMapper, times(1)).toDTO(employee2);
    }

    @Test
    void testGetEmployeeById_Success() {
        // Given
        Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setEmployeeId(1);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setDepartmentId(1);
        dto.setAvailabilityId(1);
        dto.setRoleId(1);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDTO(employee)).thenReturn(dto);

        // When
        EmployeeResponseDTO result = employeeService.getEmployeeById(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getEmployeeId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(employeeRepository, times(1)).findById(1);
        verify(employeeMapper, times(1)).toDTO(employee);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        // Given
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById(1);
        });

        assertEquals("Employee not found with ID: 1", exception.getMessage());

        verify(employeeRepository, times(1)).findById(1);
        verify(employeeMapper, never()).toDTO(any());
    }

    @Test
    void testUpdateEmployee_Success() {
        // Given
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setFirstName("Johnny");
        requestDTO.setLastName("Doe");
        requestDTO.setPassword("newpassword");
        requestDTO.setDepartmentId(2);
        requestDTO.setAvailabilityId(2);
        requestDTO.setRoleId(2);

        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeId(1);
        existingEmployee.setFirstName("John");
        existingEmployee.setLastName("Doe");
        existingEmployee.setPassword("oldpassword");

        Department existingDepartment = new Department();
        existingDepartment.setDepartmentId(1); // Set to existing department ID
        existingEmployee.setDepartment(existingDepartment);

        Availability existingAvailability = new Availability();
        existingAvailability.setAvailabilityId(1);
        existingEmployee.setAvailability(existingAvailability);

        Role existingRole = new Role();
        existingRole.setRoleId(1);
        existingEmployee.setRole(existingRole);

        Department newDepartment = new Department();
        newDepartment.setDepartmentId(2);
        newDepartment.setDepartmentName("HR");

        Availability newAvailability = new Availability();
        newAvailability.setAvailabilityId(2);
        newAvailability.setAvailabilityName("Unavailable");

        Role newRole = new Role();
        newRole.setRoleId(2);
        newRole.setRoleName("User");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setEmployeeId(1);
        updatedEmployee.setFirstName("Johnny");
        updatedEmployee.setLastName("Doe");
        updatedEmployee.setPassword("encodedNewPassword");
        updatedEmployee.setDepartment(newDepartment);
        updatedEmployee.setAvailability(newAvailability);
        updatedEmployee.setRole(newRole);

        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO();
        responseDTO.setEmployeeId(1);
        responseDTO.setFirstName("Johnny");
        responseDTO.setLastName("Doe");
        responseDTO.setDepartmentId(2);
        responseDTO.setAvailabilityId(2);
        responseDTO.setRoleId(2);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(existingEmployee));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");
        when(departmentRepository.findById(2)).thenReturn(Optional.of(newDepartment));
        when(availabilityRepository.findById(2)).thenReturn(Optional.of(newAvailability));
        when(roleRepository.findById(2)).thenReturn(Optional.of(newRole));
        when(employeeRepository.save(existingEmployee)).thenReturn(updatedEmployee);
        when(employeeMapper.toDTO(updatedEmployee)).thenReturn(responseDTO);

        // When
        EmployeeResponseDTO result = employeeService.updateEmployee(1, requestDTO);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getEmployeeId());
        assertEquals("Johnny", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(2, result.getDepartmentId());
        assertEquals(2, result.getAvailabilityId());
        assertEquals(2, result.getRoleId());

        verify(employeeRepository, times(1)).findById(1);
        verify(passwordEncoder, times(1)).encode("newpassword");
        verify(departmentRepository, times(1)).findById(2);
        verify(availabilityRepository, times(1)).findById(2);
        verify(roleRepository, times(1)).findById(2);
        verify(employeeRepository, times(1)).save(existingEmployee);
        verify(employeeMapper, times(1)).toDTO(updatedEmployee);
    }

    @Test
    void testUpdateEmployee_NotFound() {
        // Given
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setFirstName("Johnny");
        requestDTO.setLastName("Doe");
        requestDTO.setPassword("newpassword");
        requestDTO.setDepartmentId(2);
        requestDTO.setAvailabilityId(2);
        requestDTO.setRoleId(2);

        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateEmployee(1, requestDTO);
        });

        assertEquals("Employee not found with ID: 1", exception.getMessage());

        verify(employeeRepository, times(1)).findById(1);
        verify(passwordEncoder, never()).encode(anyString());
        verify(departmentRepository, never()).findById(anyInt());
        verify(availabilityRepository, never()).findById(anyInt());
        verify(roleRepository, never()).findById(anyInt());
        verify(employeeRepository, never()).save(any());
        verify(employeeMapper, never()).toDTO(any());
    }

    @Test
    void testDeleteEmployee_Success() {
        // Given
        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeId(1);
        existingEmployee.setFirstName("John");
        existingEmployee.setLastName("Doe");

        when(employeeRepository.findById(1)).thenReturn(Optional.of(existingEmployee));
        doNothing().when(employeeRepository).delete(existingEmployee);

        // When
        employeeService.deleteEmployee(1);

        // Then
        verify(employeeRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).delete(existingEmployee);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        // Given
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.deleteEmployee(1);
        });

        assertEquals("Employee not found with ID: 1", exception.getMessage());

        verify(employeeRepository, times(1)).findById(1);
        verify(employeeRepository, never()).delete(any());
    }
}
