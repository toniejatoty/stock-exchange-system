package com.stockexchange.stock_exchange_backend.controller;

import com.stockexchange.stock_exchange_backend.model.Orders;
import com.stockexchange.stock_exchange_backend.model.Portfolios;
import com.stockexchange.stock_exchange_backend.repository.OrdersRepository;
import com.stockexchange.stock_exchange_backend.repository.PortfoliosRepository;
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
    
    @Autowired
    private PortfoliosRepository portfoliosRepository;

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
        
        // Validation for SELL - check if user has enough shares
        if (order.getOrderType().equals("SELL")) {
            Portfolios portfolio = portfoliosRepository.findByUserIdAndCompanyId(
                order.getUserId(), order.getCompanyId()
            );
            
            int portfolioShares = (portfolio != null) ? portfolio.getQuantity() : 0;
            int pendingSellShares = ordersRepository.sumPendingSellQuantity(
                order.getUserId(), order.getCompanyId()
            );
            int availableShares = portfolioShares - pendingSellShares;
            
            if (availableShares < order.getQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient shares. You have: " + portfolioShares + 
                          ", pending SELL: " + pendingSellShares + 
                          ", available: " + availableShares);
            }
        }

        // Ustaw datę jeśli nie podano
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }

        // Zapisz nowe zamówienie
        Orders newOrder = ordersRepository.save(order);
        
        // Spróbuj zmatchować z istniejącymi zamówieniami
        matchOrders(newOrder);
        
        return ResponseEntity.ok(newOrder);
    }
    
    private void matchOrders(Orders newOrder) {
        if (newOrder.getStatus().equals("EXECUTED")) {
            return; // Już wykonane, nie matchuj
        }
        
        List<Orders> matchingOrders;
        
        if (newOrder.getOrderType().equals("BUY")) {
            // Szukaj SELL orders z ceną <= ceny BUY
            matchingOrders = ordersRepository.findMatchingSellOrders(
                newOrder.getCompanyId(), 
                newOrder.getPrice()
            );
        } else {
            // Szukaj BUY orders z ceną >= ceny SELL
            matchingOrders = ordersRepository.findMatchingBuyOrders(
                newOrder.getCompanyId(), 
                newOrder.getPrice()
            );
        }
        
        // Próbuj zmatchować z każdym pasującym zamówieniem
        for (Orders matchingOrder : matchingOrders) {
            if (newOrder.getStatus().equals("EXECUTED")) {
                break; // Nowe zamówienie już w pełni wykonane
            }
            
            int newQty = newOrder.getQuantity();
            int matchQty = matchingOrder.getQuantity();
            
            if (newQty == matchQty) {
                // Exact match - oba zamówienia w pełni wykonane
                newOrder.setStatus("EXECUTED");
                newOrder.setPrice(matchingOrder.getPrice()); // Wykonaj po cenie z matchującego
                newOrder.setOrderDate(LocalDateTime.now());
                
                matchingOrder.setStatus("EXECUTED");
                matchingOrder.setOrderDate(LocalDateTime.now());
                
                ordersRepository.save(newOrder);
                ordersRepository.save(matchingOrder);
                
                // Aktualizuj portfolios
                updatePortfolio(newOrder);
                updatePortfolio(matchingOrder);
                
            } else if (newQty < matchQty) {
                // Nowe zamówienie mniejsze - nowe EXECUTED, stare zmniejsz
                newOrder.setStatus("EXECUTED");
                newOrder.setPrice(matchingOrder.getPrice()); // Wykonaj po cenie z matchującego
                newOrder.setOrderDate(LocalDateTime.now());
                ordersRepository.save(newOrder);
                
                // Zmniejsz ilość w starym zamówieniu (pozostaje PENDING)
                matchingOrder.setQuantity(matchQty - newQty);
                ordersRepository.save(matchingOrder);
                
                // Utwórz rekord EXECUTED dla częściowo wykonanej części starego zamówienia
                Orders executedPart = new Orders();
                executedPart.setUserId(matchingOrder.getUserId());
                executedPart.setCompanyId(matchingOrder.getCompanyId());
                executedPart.setOrderType(matchingOrder.getOrderType());
                executedPart.setQuantity(newQty);
                executedPart.setPrice(matchingOrder.getPrice());
                executedPart.setOrderDate(LocalDateTime.now());
                executedPart.setStatus("EXECUTED");
                ordersRepository.save(executedPart);
                
                // Aktualizuj portfolios
                updatePortfolio(newOrder);
                updatePortfolio(executedPart);
                
            } else {
                // Nowe zamówienie większe - stare EXECUTED, nowe zmniejsz
                matchingOrder.setStatus("EXECUTED");
                matchingOrder.setOrderDate(LocalDateTime.now());
                ordersRepository.save(matchingOrder);
                
                // Zmniejsz ilość w nowym zamówieniu (pozostaje PENDING)
                newOrder.setQuantity(newQty - matchQty);
                ordersRepository.save(newOrder);
                
                // Utwórz rekord EXECUTED dla częściowo wykonanej części nowego zamówienia
                Orders executedPart = new Orders();
                executedPart.setUserId(newOrder.getUserId());
                executedPart.setCompanyId(newOrder.getCompanyId());
                executedPart.setOrderType(newOrder.getOrderType());
                executedPart.setQuantity(matchQty);
                executedPart.setPrice(matchingOrder.getPrice());
                executedPart.setOrderDate(LocalDateTime.now());
                executedPart.setStatus("EXECUTED");
                ordersRepository.save(executedPart);
                
                // Aktualizuj portfolios
                updatePortfolio(matchingOrder);
                updatePortfolio(executedPart);
            }
        }
    }
    
    private void updatePortfolio(Orders executedOrder) {
        Portfolios portfolio = portfoliosRepository.findByUserIdAndCompanyId(
            executedOrder.getUserId(), 
            executedOrder.getCompanyId()
        );
        
        if (executedOrder.getOrderType().equals("BUY")) {
            // BUY - dodaj akcje
            if (portfolio == null) {
                // Utwórz nowy rekord
                portfolio = new Portfolios();
                portfolio.setUserId(executedOrder.getUserId());
                portfolio.setCompanyId(executedOrder.getCompanyId());
                portfolio.setQuantity(executedOrder.getQuantity());
                portfolio.setLastUpdated(LocalDateTime.now());
            } else {
                // Dodaj do istniejącego
                portfolio.setQuantity(portfolio.getQuantity() + executedOrder.getQuantity());
                portfolio.setLastUpdated(LocalDateTime.now());
            }
            portfoliosRepository.save(portfolio);
            
        } else if (executedOrder.getOrderType().equals("SELL")) {
            // SELL - odejmij akcje
            if (portfolio != null) {
                int newQuantity = portfolio.getQuantity() - executedOrder.getQuantity();
                if (newQuantity <= 0) {
                    // Usuń rekord jeśli ilość = 0
                    portfoliosRepository.deleteById(portfolio.getId());
                } else {
                    // Zaktualizuj ilość
                    portfolio.setQuantity(newQuantity);
                    portfolio.setLastUpdated(LocalDateTime.now());
                    portfoliosRepository.save(portfolio);
                }
            }
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        return ordersRepository.findById(id)
            .map(order -> {
                if (order.getStatus().equals("CANCELLED")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Order is already cancelled");
                }
                if (order.getStatus().equals("EXECUTED")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Cannot cancel executed order");
                }
                
                order.setStatus("CANCELLED");
                ordersRepository.save(order);
                return ResponseEntity.ok(order);
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Zamówienie o ID " + id + " nie istnieje"));
    }
}
