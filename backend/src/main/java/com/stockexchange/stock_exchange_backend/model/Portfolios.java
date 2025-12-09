package com.stockexchange.stock_exchange_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PORTFOLIOS")
public class Portfolios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "company_id", nullable = false)
    private Long companyId;
    
    @Column(name = "quantity", nullable = false)
    
    private Integer quantity;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    // Konstruktory
    public Portfolios() {}
    
    public Portfolios(Long userId, Long companyId, Integer quantity) {
        this.userId = userId;
        this.companyId = companyId;
        this.quantity = quantity;
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Gettery i Settery
    public Long getId() { return id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { 
        this.lastUpdated = lastUpdated; 
    }
}