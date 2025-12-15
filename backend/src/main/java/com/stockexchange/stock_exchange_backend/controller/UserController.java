package com.stockexchange.stock_exchange_backend.controller;

import com.stockexchange.stock_exchange_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stockexchange.stock_exchange_backend.model.Users;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/fullnames")
    public List<String> getAllFullNames() {
        return userRepository.findAllFullNames();
    }
    
    @GetMapping
    public List<Users> getAllUsers(){
        return userRepository.findAllUsers();
    }
    
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody Users user) {
        // Walidacja formatu email
        if (user.getEmail() == null || !user.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Podaj poprawny adres email (np. user@example.com)");
        }
        
        // Sprawdź czy email już istnieje
        Integer count = userRepository.countByEmail(user.getEmail());
        
        if (count != null && count > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Użytkownik z emailem '" + user.getEmail() + "' już istnieje!");
        }
        
        Users saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        // Sprawdź czy użytkownik ma portfolios
        Integer count = userRepository.countPortfoliosByUserId(id);
        
        if (count != null && count > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Nie można usunąć użytkownika - ma " + count + " portfolio(s)");
        }
        
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}