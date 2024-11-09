package com.example.taskmanager.repository;

import com.example.taskmanager.entity.EmployeeTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeTaskRepository extends JpaRepository<EmployeeTask, Integer> {
    // Custom query methods (if any) go here
}