package com.example.wso2demo.controller;

import com.example.wso2demo.model.ApiResponse;
import com.example.wso2demo.model.User;
import com.example.wso2demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management APIs with support for headers and query parameters")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    @Operation(
        summary = "Get all users with optional filtering",
        description = "Retrieve users with optional filters: country, city, status. Supports custom headers."
    )
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(
            @Parameter(description = "Filter by country") @RequestParam(required = false) String country,
            @Parameter(description = "Filter by city") @RequestParam(required = false) String city,
            @Parameter(description = "Filter by status (ACTIVE, INACTIVE)") @RequestParam(required = false) String status,
            @Parameter(description = "Client ID for tracking") @RequestHeader(value = "X-Client-ID", required = false) String clientId,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId,
            @Parameter(description = "API version") @RequestHeader(value = "X-API-Version", required = false, defaultValue = "1.0") String apiVersion
    ) {
        List<User> users = userService.getFilteredUsers(country, city, status);
        
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .header("X-API-Version", apiVersion)
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(users, "Users retrieved successfully"));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    public ResponseEntity<ApiResponse<User>> getUserById(
            @Parameter(description = "User ID", required = true) @PathVariable Long id,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok()
                        .header("X-Request-ID", requestId != null ? requestId : "N/A")
                        .body(ApiResponse.success(user, "User found")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found with id: " + id)));
    }
    
    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieve a user by their username")
    public ResponseEntity<ApiResponse<User>> getUserByUsername(
            @Parameter(description = "Username", required = true) @PathVariable String username,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        return userService.getUserByUsername(username)
                .map(user -> ResponseEntity.ok()
                        .header("X-Request-ID", requestId != null ? requestId : "N/A")
                        .body(ApiResponse.success(user, "User found")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found with username: " + username)));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search users by name", description = "Search users by full name (case-insensitive)")
    public ResponseEntity<ApiResponse<List<User>>> searchUsers(
            @Parameter(description = "Name to search for", required = true) @RequestParam String name,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        List<User> users = userService.searchUsersByName(name);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(users, "Search completed"));
    }
    
    @GetMapping("/country/{country}")
    @Operation(summary = "Get users by country", description = "Retrieve all users from a specific country")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByCountry(
            @Parameter(description = "Country name", required = true) @PathVariable String country,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        List<User> users = userService.getUsersByCountry(country);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(users, "Users retrieved for country: " + country));
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get users by status", description = "Retrieve all users with a specific status")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByStatus(
            @Parameter(description = "Status (ACTIVE, INACTIVE)", required = true) @PathVariable String status,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        List<User> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(users, "Users retrieved for status: " + status));
    }
    
    @GetMapping("/countries")
    @Operation(summary = "Get all countries", description = "Retrieve a list of all countries")
    public ResponseEntity<ApiResponse<List<String>>> getAllCountries(
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        List<String> countries = userService.getAllCountries();
        return ResponseEntity.ok()
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(countries, "Countries retrieved successfully"));
    }
    
    @PostMapping
    @Operation(summary = "Create a new user", description = "Register a new user")
    public ResponseEntity<ApiResponse<User>> createUser(
            @Valid @RequestBody User user,
            @Parameter(description = "Client ID") @RequestHeader(value = "X-Client-ID", required = false) String clientId,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .header("Location", "/api/v1/users/" + createdUser.getId())
                .body(ApiResponse.success(createdUser, "User created successfully"));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Update an existing user by ID")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @Parameter(description = "User ID", required = true) @PathVariable Long id,
            @Valid @RequestBody User user,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        return userService.updateUser(id, user)
                .map(updatedUser -> ResponseEntity.ok()
                        .header("X-Request-ID", requestId != null ? requestId : "N/A")
                        .body(ApiResponse.success(updatedUser, "User updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found with id: " + id)));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Remove a user from the system")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "User ID", required = true) @PathVariable Long id,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok()
                    .header("X-Request-ID", requestId != null ? requestId : "N/A")
                    .body(ApiResponse.success(null, "User deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("User not found with id: " + id));
    }
}
