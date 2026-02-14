package com.example.wso2demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Health", description = "Health check and status APIs")
public class HealthCheckController {
    
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the API is running")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "WSO2 APIM Demo API");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/info")
    @Operation(summary = "API information", description = "Get information about the API")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "WSO2 APIM Demo API");
        response.put("description", "Demo Spring Boot REST API for WSO2 API Manager integration");
        response.put("version", "1.0.0");
        response.put("endpoints", new String[]{
            "GET /api/v1/products",
            "GET /api/v1/products/{id}",
            "POST /api/v1/products",
            "PUT /api/v1/products/{id}",
            "DELETE /api/v1/products/{id}",
            "GET /api/v1/users",
            "GET /api/v1/users/{id}",
            "POST /api/v1/users",
            "PUT /api/v1/users/{id}",
            "DELETE /api/v1/users/{id}"
        });
        return ResponseEntity.ok(response);
    }
}
