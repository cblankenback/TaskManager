package com.example.taskmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EMPLOYEENOTIFICATION")
public class EmployeeNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLOYEENOTIFICATION_ID", nullable = false)
    private Integer employeeNotificationId;

    @Column(name = "ISREAD", nullable = false)
    private Boolean isRead;

    @Column(name = "RECEIVED")
    private LocalDateTime received;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTIFICATION_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Employee employee;
}
