package com.example.taskmanager.controller;

import com.example.taskmanager.dto.EmployeeResponseDTO;
import com.example.taskmanager.entity.Employee;
import com.example.taskmanager.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/me")
    public EmployeeResponseDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = (Employee) authentication.getPrincipal();
        return employeeService.convertToResponseDTO(employee);
    }
}
