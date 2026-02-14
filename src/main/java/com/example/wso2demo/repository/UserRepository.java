package com.example.wso2demo.repository;

import com.example.wso2demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findByStatus(String status);
    
    List<User> findByCountry(String country);
    
    List<User> findByCity(String city);
    
    List<User> findByFullNameContainingIgnoreCase(String name);
    
    @Query("SELECT DISTINCT u.country FROM User u WHERE u.country IS NOT NULL ORDER BY u.country")
    List<String> findAllCountries();
    
    @Query("SELECT DISTINCT u.city FROM User u WHERE u.city IS NOT NULL ORDER BY u.city")
    List<String> findAllCities();
}
