package com.stockexchange.stock_exchange_backend.controller;

import com.stockexchange.stock_exchange_backend.model.Categories;
import com.stockexchange.stock_exchange_backend.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    
    // Dodaj nową kategorię
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Categories category) {
        // Walidacja nazwy
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Category name cannot be empty");
        }
        
        // Check name uniqueness
        Long existingCount = categoriesRepository.countByName(category.getName());
        if (existingCount > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Category with name '" + category.getName() + "' already exists");
        }
        
        if (category.getDescription() == null || category.getDescription().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Category description cannot be empty");
        }
        
        Categories saved = categoriesRepository.save(category);
        return ResponseEntity.ok(saved);
    }
    
    // Usuń kategorię
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        // Sprawdź czy kategoria istnieje
        if (!categoriesRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Category with ID " + id + " not found");
        }
        
        // Check if there are linked companies
        Integer companiesCount = categoriesRepository.countCompaniesByCategoryId(id);
        if (companiesCount > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Cannot delete category - it is linked to " + companiesCount + " company(ies)");
        }
        
        categoriesRepository.deleteById(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

}