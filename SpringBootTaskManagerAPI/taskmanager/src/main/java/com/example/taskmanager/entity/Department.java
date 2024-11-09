package com.example.taskmanager.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString
@Table(name = "DEPARTMENT")

public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPARTMENT_ID", nullable = false)
    private Integer departmentId;
    
    @Setter
    @Column(name = "DEPARTMENT_NAME", nullable = false, length = 45)
    private String departmentName;
}
