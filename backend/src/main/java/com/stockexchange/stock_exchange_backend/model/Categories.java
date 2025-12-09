package com.stockexchange.stock_exchange_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORIES")
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 50)
    private String name;
   
    @Column(name = "description", nullable = false, length = 255)
    private String description;
   
    public Categories() {}
    
    public Categories(String name, String description, Long categoryId) {
        this.name = name;
        this.description = description;
    }
    
    // Gettery i Settery
    public Long getId() { return id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
    
}