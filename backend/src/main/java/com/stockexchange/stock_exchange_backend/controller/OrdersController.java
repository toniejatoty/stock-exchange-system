package com.stockexchange.stock_exchange_backend.controller;

import com.stockexchange.stock_exchange_backend.model.Orders;
import com.stockexchange.stock_exchange_backend.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrdersController {

    @Autowired
    private OrdersRepository ordersRepository;

    @GetMapping
    public List<Orders> getAllOrders() {
        return ordersRepository.findAllOrders();
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Orders order) {
        // Walidacja orderType
        if (order.getOrderType() == null || 
            (!order.getOrderType().equals("BUY") && !order.getOrderType().equals("SELL"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Order type musi być 'BUY' lub 'SELL'");
        }

        // Walidacja status
        if (order.getStatus() == null || order.getStatus().isEmpty()) {
            order.setStatus("PENDING"); // Domyślny status
        } else if (!order.getStatus().equals("PENDING") && 
                   !order.getStatus().equals("EXECUTED") && 
                   !order.getStatus().equals("CANCELLED")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Status musi być 'PENDING', 'EXECUTED' lub 'CANCELLED'");
        }

        // Walidacja quantity
        if (order.getQuantity() == null || order.getQuantity() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Quantity musi być większe od 0");
        }

        // Walidacja price
        if (order.getPrice() == null || order.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Price musi być większe od 0");
        }

        // Ustaw datę jeśli nie podano
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }

        Orders saved = ordersRepository.save(order);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        return ordersRepository.findById(id)
            .map(order -> {
                if (order.getStatus().equals("CANCELLED")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Zamówienie jest już anulowane");
                }
                if (order.getStatus().equals("EXECUTED")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Nie można anulować wykonanego zamówienia");
                }
                
                order.setStatus("CANCELLED");
                ordersRepository.save(order);
                return ResponseEntity.ok(order);
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Zamówienie o ID " + id + " nie istnieje"));
    }
}
