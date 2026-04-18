package com.example.ad.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ad.model.User;



public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);  // To check if email already exists (optional)
    
    //User findByEmail(String email);  // Method to find user by email
   Optional<User> findByEmail(String email);
}
//test q2