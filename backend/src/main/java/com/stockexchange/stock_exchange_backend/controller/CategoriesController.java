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
                .body("Nazwa kategorii nie może być pusta");
        }
        
        // Sprawdź uniklaność nazwy
        Long existingCount = categoriesRepository.countByName(category.getName());
        if (existingCount > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Kategoria o nazwie '" + category.getName() + "' już istnieje");
        }
        
        if (category.getDescription() == null || category.getDescription().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Opis kategorii nie może być pusty");
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
                .body("Kategoria o ID " + id + " nie istnieje");
        }
        
        // Sprawdź czy są powiązane spółki
        Integer companiesCount = categoriesRepository.countCompaniesByCategoryId(id);
        if (companiesCount > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Nie można usunąć kategorii - jest powiązana z " + companiesCount + " spółką/ami");
        }
        
        categoriesRepository.deleteById(id);
        return ResponseEntity.ok("Kategoria usunięta pomyślnie");
    }

}