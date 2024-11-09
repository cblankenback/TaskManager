package com.example.taskmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;



@Entity
@Setter
@Getter
@ToString
@Table(name = "AVAILABILITY")
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AVAILABILITY_ID", nullable = false)
    private Integer availabilityId;

    @Column(name = "AVAILABILITY_NAME", nullable = false, length = 45)
    private String availabilityName;

    // Optional: Bidirectional relationship with Employee
    @OneToMany(mappedBy = "availability", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();
}
