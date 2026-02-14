package com.example.wso2demo.service;

import com.example.wso2demo.model.User;
import com.example.wso2demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @PostConstruct
    public void init() {
        // Sample data is loaded from complete-mysql-setup.sql script
        // No need to initialize here
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public List<User> getUsersByCountry(String country) {
        return userRepository.findByCountry(country);
    }
    
    public List<User> getUsersByCity(String city) {
        return userRepository.findByCity(city);
    }
    
    public List<User> getUsersByStatus(String status) {
        return userRepository.findByStatus(status);
    }
    
    public List<User> searchUsersByName(String name) {
        return userRepository.findByFullNameContainingIgnoreCase(name);
    }
    
    public List<String> getAllCountries() {
        return userRepository.findAllCountries();
    }
    
    public List<String> getAllCities() {
        return userRepository.findAllCities();
    }
    
    public List<User> getFilteredUsers(String country, String city, String status) {
        List<User> users = userRepository.findAll();
        
        if (country != null && !country.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getCountry() != null && u.getCountry().equalsIgnoreCase(country))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        if (city != null && !city.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getCity() != null && u.getCity().equalsIgnoreCase(city))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        if (status != null && !status.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getStatus() != null && u.getStatus().equalsIgnoreCase(status))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        return users;
    }
    
    public User createUser(User user) {
        user.setId(null); // Ensure new user
        return userRepository.save(user);
    }
    
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(updatedUser.getUsername());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setFullName(updatedUser.getFullName());
                    existingUser.setPhone(updatedUser.getPhone());
                    existingUser.setAddress(updatedUser.getAddress());
                    existingUser.setCity(updatedUser.getCity());
                    existingUser.setCountry(updatedUser.getCountry());
                    existingUser.setStatus(updatedUser.getStatus());
                    return userRepository.save(existingUser);
                });
    }
    
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
