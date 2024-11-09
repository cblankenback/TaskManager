package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Integer> {
    // Custom query methods (if any) go here
}