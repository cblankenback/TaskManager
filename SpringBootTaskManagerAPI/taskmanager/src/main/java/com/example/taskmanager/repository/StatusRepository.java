package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    // Custom query methods (if any) go here
}
