package com.example.taskmanager.repository;

import com.example.taskmanager.entity.TaskUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskUpdateRepository extends JpaRepository<TaskUpdate, Integer> {
    // Custom query methods (if any) go here
}