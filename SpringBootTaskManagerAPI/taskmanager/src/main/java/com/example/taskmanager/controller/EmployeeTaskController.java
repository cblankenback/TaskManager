package com.example.taskmanager.controller;

import com.example.taskmanager.dto.EmployeeTaskRequestDTO;
import com.example.taskmanager.dto.EmployeeTaskResponseDTO;
import com.example.taskmanager.service.EmployeeTaskService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employeetasks")
public class EmployeeTaskController {

    private final EmployeeTaskService employeeTaskService;

    public EmployeeTaskController(EmployeeTaskService employeeTaskService) {
        this.employeeTaskService = employeeTaskService;
    }

    // Create a new employee task
    @PostMapping
    public ResponseEntity<EmployeeTaskResponseDTO> createEmployeeTask(@Valid @RequestBody EmployeeTaskRequestDTO requestDTO) {
        EmployeeTaskResponseDTO createdEmployeeTask = employeeTaskService.createEmployeeTask(requestDTO);
        return new ResponseEntity<>(createdEmployeeTask, HttpStatus.CREATED);
    }

    // Retrieve all employee tasks
    @GetMapping
    public ResponseEntity<List<EmployeeTaskResponseDTO>> getAllEmployeeTasks() {
        List<EmployeeTaskResponseDTO> employeeTasks = employeeTaskService.getAllEmployeeTasks();
        return new ResponseEntity<>(employeeTasks, HttpStatus.OK);
    }

    // Retrieve employee task by ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeTaskResponseDTO> getEmployeeTaskById(@PathVariable Integer id) {
        EmployeeTaskResponseDTO employeeTask = employeeTaskService.getEmployeeTaskById(id);
        return new ResponseEntity<>(employeeTask, HttpStatus.OK);
    }

    // Update an existing employee task
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeTaskResponseDTO> updateEmployeeTask(@PathVariable Integer id,
                                                                        @Valid @RequestBody EmployeeTaskRequestDTO requestDTO) {
        EmployeeTaskResponseDTO updatedEmployeeTask = employeeTaskService.updateEmployeeTask(id, requestDTO);
        return new ResponseEntity<>(updatedEmployeeTask, HttpStatus.OK);
    }

    // Delete an employee task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeTask(@PathVariable Integer id) {
        employeeTaskService.deleteEmployeeTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
