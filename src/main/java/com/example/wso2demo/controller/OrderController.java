package com.example.wso2demo.controller;

import com.example.wso2demo.model.ApiResponse;
import com.example.wso2demo.model.Order;
import com.example.wso2demo.service.OrderService;
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
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Order management APIs with support for headers")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders")
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders(
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId,
            @Parameter(description = "API version") @RequestHeader(value = "X-API-Version", required = false, defaultValue = "1.0") String apiVersion
    ) {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(orders.size()))
                .header("X-API-Version", apiVersion)
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(orders, "Orders retrieved successfully"));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    public ResponseEntity<ApiResponse<Order>> getOrderById(
            @Parameter(description = "Order ID", required = true) @PathVariable Long id,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.ok()
                        .header("X-Request-ID", requestId != null ? requestId : "N/A")
                        .body(ApiResponse.success(order, "Order found")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Order not found with id: " + id)));
    }
    
    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by order number", description = "Retrieve an order by its order number")
    public ResponseEntity<ApiResponse<Order>> getOrderByOrderNumber(
            @Parameter(description = "Order number", required = true) @PathVariable String orderNumber,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        return orderService.getOrderByOrderNumber(orderNumber)
                .map(order -> ResponseEntity.ok()
                        .header("X-Request-ID", requestId != null ? requestId : "N/A")
                        .body(ApiResponse.success(order, "Order found")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Order not found with number: " + orderNumber)));
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get orders by user ID", description = "Retrieve all orders for a specific user")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByUserId(
            @Parameter(description = "User ID", required = true) @PathVariable Long userId,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(orders.size()))
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(orders, "Orders retrieved for user: " + userId));
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", description = "Retrieve all orders with a specific status")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByStatus(
            @Parameter(description = "Order status (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)", required = true) @PathVariable String status,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(orders.size()))
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(orders, "Orders retrieved for status: " + status));
    }
    
    @GetMapping("/statuses")
    @Operation(summary = "Get all order statuses", description = "Retrieve a list of all order statuses")
    public ResponseEntity<ApiResponse<List<String>>> getAllStatuses(
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        List<String> statuses = orderService.getAllStatuses();
        return ResponseEntity.ok()
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .body(ApiResponse.success(statuses, "Statuses retrieved successfully"));
    }
    
    @PostMapping
    @Operation(summary = "Create a new order", description = "Place a new order")
    public ResponseEntity<ApiResponse<Order>> createOrder(
            @Valid @RequestBody Order order,
            @Parameter(description = "Client ID") @RequestHeader(value = "X-Client-ID", required = false) String clientId,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("X-Request-ID", requestId != null ? requestId : "N/A")
                .header("Location", "/api/v1/orders/" + createdOrder.getId())
                .body(ApiResponse.success(createdOrder, "Order created successfully"));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an order", description = "Update an existing order by ID")
    public ResponseEntity<ApiResponse<Order>> updateOrder(
            @Parameter(description = "Order ID", required = true) @PathVariable Long id,
            @Valid @RequestBody Order order,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        return orderService.updateOrder(id, order)
                .map(updatedOrder -> ResponseEntity.ok()
                        .header("X-Request-ID", requestId != null ? requestId : "N/A")
                        .body(ApiResponse.success(updatedOrder, "Order updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Order not found with id: " + id)));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order", description = "Remove an order from the system")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(
            @Parameter(description = "Order ID", required = true) @PathVariable Long id,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId
    ) {
        boolean deleted = orderService.deleteOrder(id);
        if (deleted) {
            return ResponseEntity.ok()
                    .header("X-Request-ID", requestId != null ? requestId : "N/A")
                    .body(ApiResponse.success(null, "Order deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Order not found with id: " + id));
    }
}
