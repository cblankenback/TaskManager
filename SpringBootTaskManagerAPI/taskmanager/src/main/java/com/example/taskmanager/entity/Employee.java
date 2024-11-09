package com.example.taskmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Setter
@Getter
@ToString
@Table(name = "EMPLOYEE")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLOYEE_ID", nullable = false)
    private Integer employeeId;

    @Column(name = "FIRST_NAME", nullable = false, length = 45)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 45)
    private String lastName;

    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT) // Prevents deletion if referenced
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AVAILABILITY_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Availability availability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Role role;

    // Optional: Bidirectional relationship with Task
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> createdTasks = new ArrayList<>();
}
