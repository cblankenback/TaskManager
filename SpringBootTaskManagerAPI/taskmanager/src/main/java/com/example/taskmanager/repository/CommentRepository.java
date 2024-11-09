package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    // Custom query methods (if any) go here
}
