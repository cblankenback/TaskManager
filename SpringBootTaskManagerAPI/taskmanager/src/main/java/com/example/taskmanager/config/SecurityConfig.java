package com.example.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Defines the password encoder bean using BCrypt.
     *
     * @return PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines the in-memory user details service with a default admin user.
     *
     * @param passwordEncoder PasswordEncoder to encode the password.
     * @return UserDetailsService instance.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("adminpassword"))
                .roles("ADMIN")
                .build();

        // You can define more users here if needed

        return new InMemoryUserDetailsManager(admin);
    }

    /**
     * Configures the security filter chain.
     *
     * @param http HttpSecurity instance.
     * @return SecurityFilterChain.
     * @throws Exception if an error occurs.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        http
            // Disable CSRF for simplicity; enable it in production
            .csrf(csrf -> csrf.disable())
            
            // Authorize requests
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Secure admin endpoints
                .requestMatchers("/api/**").authenticated()        // Secure all API endpoints
                .anyRequest().permitAll()                           // Allow other requests
            )
            
            // Enable HTTP Basic Authentication
            .httpBasic(Customizer.withDefaults());
        
        return http.build();
    }
}
