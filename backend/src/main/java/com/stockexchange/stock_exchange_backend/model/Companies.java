package com.stockexchange.stock_exchange_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "COMPANIES")
public class Companies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "symbol", nullable = false, unique = true, length = 10)
    private String symbol;
    
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    
    public Companies() {}
    
    public Companies(String name, String symbol, Long categoryId) {
        this.name = name;
        this.symbol = symbol;
        this.categoryId = categoryId;
    }
    
    // Gettery i Settery
    public Long getId() { return id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}