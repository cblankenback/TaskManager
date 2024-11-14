package com.example.taskmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EMPLOYEE")
public class Employee implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLOYEE_ID", nullable = false)
    private Integer employeeId;
    
    @Getter
    @Column(name = "USERNAME", nullable = false, unique = true, length = 45)
    private String username;

    @Column(name = "FIRST_NAME", nullable = false, length = 45)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 45)
    private String lastName;

    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT) // Prevents deletion if referenced
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AVAILABILITY_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Availability availability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Role role;

    // Optional: Bidirectional relationship with Task
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> createdTasks = new ArrayList<>();
    
    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // You can map roles or authorities here
        return null; // Implement this based on your roles
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement logic if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement logic if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement logic if needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Implement logic if needed
    }

	
	
}
