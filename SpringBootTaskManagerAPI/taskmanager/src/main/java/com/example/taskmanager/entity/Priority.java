package com.example.taskmanager.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString
@Table(name = "PRIORITY")

public class Priority {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRIORITY_ID", nullable = false)
    private Integer priorityId;
     
    @Setter
    @Column(name = "TYPE", nullable = false, length = 45)
    private String type;
}
