package com.example.taskmanager.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TASK")

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TASK_ID", nullable = false)
    private Integer taskId;

   
    @Column(name = "TASK_NAME", nullable = false, length = 45)
    private String taskName;

    
    @Column(name = "DESCRIPTION", nullable = false, length = 255)
    private String description;

   
    @Column(name = "DEADLINE", nullable = false)
    private LocalDateTime deadline;

  
    @Column(name = "CREATION_DATE" , nullable = false, updatable = false)
    private LocalDateTime creationDate;
    
    @Column(name = "ADDRESS" , nullable = false, updatable = false)
    private String address;

   
    @Column(name = "ARCHIVED", nullable = false)
    private Boolean archived = false;

    // Self-referencing many-to-one relationship for dependency
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPENDENCY_TASK_ID", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL) // Aligns with ON DELETE SET NULL
    @JsonBackReference
    private Task dependencyTask;

    // Many-to-one relationship with Employee (creator)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // Aligns with ON DELETE CASCADE
    private Employee createdBy;

    // Many-to-one relationship with Priority
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRIORITY_ID")
    @OnDelete(action = OnDeleteAction.SET_NULL) // Aligns with ON DELETE SET NULL
    private Priority priority;

    // One-to-many relationship for tasks that depend on this task
    @OneToMany(mappedBy = "dependencyTask", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Task> dependentTasks = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
        System.out.println("Task creationDate set to: " + this.creationDate);
    }

	
}
