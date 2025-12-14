package com.stockexchange.stock_exchange_backend.controller;

import com.stockexchange.stock_exchange_backend.model.Categories;
import com.stockexchange.stock_exchange_backend.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoriesController {
    
    @Autowired
    private CategoriesRepository categoriesRepository;
    
    // Pobierz wszystkie spółki
    @GetMapping
    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

}