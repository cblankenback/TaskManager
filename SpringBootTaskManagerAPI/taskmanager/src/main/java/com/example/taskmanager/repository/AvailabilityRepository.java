package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {
    // Custom query methods (if any) go here
}