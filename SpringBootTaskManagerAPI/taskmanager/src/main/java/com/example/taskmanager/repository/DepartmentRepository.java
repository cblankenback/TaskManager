package com.example.taskmanager.repository;


import com.example.taskmanager.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    // Custom query methods (if any) go here
}
