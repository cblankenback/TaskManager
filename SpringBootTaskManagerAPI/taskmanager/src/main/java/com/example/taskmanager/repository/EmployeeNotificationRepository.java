package com.example.taskmanager.repository;

import com.example.taskmanager.entity.EmployeeNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeNotificationRepository extends JpaRepository<EmployeeNotification, Integer> {
    // Custom query methods (if any) go here
}