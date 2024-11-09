package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    // Custom query methods (if any) go here
}
