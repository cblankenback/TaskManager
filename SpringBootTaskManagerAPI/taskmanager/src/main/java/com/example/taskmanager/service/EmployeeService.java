package com.example.taskmanager.service;

import com.example.taskmanager.dto.EmployeeRequestDTO;
import com.example.taskmanager.dto.EmployeeResponseDTO;
import com.example.taskmanager.entity.Employee;
import com.example.taskmanager.entity.Department;
import com.example.taskmanager.entity.Availability;
import com.example.taskmanager.entity.Role;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.mapper.EmployeeMapper;
import com.example.taskmanager.repository.EmployeeRepository;
import com.example.taskmanager.repository.DepartmentRepository;
import com.example.taskmanager.repository.AvailabilityRepository;
import com.example.taskmanager.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;
    private final AvailabilityRepository availabilityRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeMapper employeeMapper,
                           DepartmentRepository departmentRepository,
                           AvailabilityRepository availabilityRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.departmentRepository = departmentRepository;
        this.availabilityRepository = availabilityRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create a new employee
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {

        // Validate that password is provided
        if (requestDTO.getPassword() == null || requestDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required when creating an employee");
        }

        Employee employee = employeeMapper.toEntity(requestDTO);

        // Encode password
        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());
        employee.setPassword(encodedPassword);

        // Fetch and set Department
        Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + requestDTO.getDepartmentId()));
        employee.setDepartment(department);

        // Fetch and set Availability
        Availability availability = availabilityRepository.findById(requestDTO.getAvailabilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found with ID: " + requestDTO.getAvailabilityId()));
        employee.setAvailability(availability);

        // Fetch and set Role
        Role role = roleRepository.findById(requestDTO.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + requestDTO.getRoleId()));
        employee.setRole(role);

        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toDTO(savedEmployee);
    }

    // Retrieve all employees
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Retrieve employee by ID
    public EmployeeResponseDTO getEmployeeById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        return employeeMapper.toDTO(employee);
    }

    // Update an existing employee
    @Transactional
    public EmployeeResponseDTO updateEmployee(Integer id, EmployeeRequestDTO requestDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        existingEmployee.setFirstName(requestDTO.getFirstName());
        existingEmployee.setLastName(requestDTO.getLastName());

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            // Encode new password
            String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());
            existingEmployee.setPassword(encodedPassword);
        }

        // Fetch and set Department if changed
        if (!existingEmployee.getDepartment().getDepartmentId().equals(requestDTO.getDepartmentId())) {
            Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + requestDTO.getDepartmentId()));
            existingEmployee.setDepartment(department);
        }

        // Fetch and set Availability if changed
        if (!existingEmployee.getAvailability().getAvailabilityId().equals(requestDTO.getAvailabilityId())) {
            Availability availability = availabilityRepository.findById(requestDTO.getAvailabilityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Availability not found with ID: " + requestDTO.getAvailabilityId()));
            existingEmployee.setAvailability(availability);
        }

        // Fetch and set Role if changed
        if (!existingEmployee.getRole().getRoleId().equals(requestDTO.getRoleId())) {
            Role role = roleRepository.findById(requestDTO.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + requestDTO.getRoleId()));
            existingEmployee.setRole(role);
        }

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        return employeeMapper.toDTO(updatedEmployee);
    }

    // Delete an employee
    @Transactional
    public void deleteEmployee(Integer id) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        employeeRepository.delete(existingEmployee);
    }
}
