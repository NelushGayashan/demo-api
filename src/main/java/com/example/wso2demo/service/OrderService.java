package com.example.wso2demo.service;

import com.example.wso2demo.model.Order;
import com.example.wso2demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    public Optional<Order> getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
    
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
    
    public List<String> getAllStatuses() {
        return orderRepository.findAllStatuses();
    }
    
    public Order createOrder(Order order) {
        order.setId(null); // Ensure new order
        return orderRepository.save(order);
    }
    
    public Optional<Order> updateOrder(Long id, Order updatedOrder) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setOrderNumber(updatedOrder.getOrderNumber());
                    existingOrder.setUserId(updatedOrder.getUserId());
                    existingOrder.setTotalAmount(updatedOrder.getTotalAmount());
                    existingOrder.setStatus(updatedOrder.getStatus());
                    existingOrder.setPaymentMethod(updatedOrder.getPaymentMethod());
                    existingOrder.setShippingAddress(updatedOrder.getShippingAddress());
                    return orderRepository.save(existingOrder);
                });
    }
    
    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
