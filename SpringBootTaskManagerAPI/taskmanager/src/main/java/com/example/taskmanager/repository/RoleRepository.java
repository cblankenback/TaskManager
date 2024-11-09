package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Custom query methods (if any) go here
}
