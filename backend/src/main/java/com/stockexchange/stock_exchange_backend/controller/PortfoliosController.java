package com.stockexchange.stock_exchange_backend.controller;

import com.stockexchange.stock_exchange_backend.model.Portfolios;
import com.stockexchange.stock_exchange_backend.repository.PortfoliosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
@CrossOrigin(origins = "http://localhost:4200")
public class PortfoliosController {
    
    @Autowired
    private PortfoliosRepository portfoliosRepository;
    
    // Pobierz wszystkie portfolio
    @GetMapping
    public List<Portfolios> getAllPortfolios() {
        return portfoliosRepository.findAll();
    }
    
    // Pobierz portfolio użytkownika po ID
    @GetMapping("/user/{userId}")
    public List<Portfolios> getPortfolioByUserId(@PathVariable Long userId) {
        return portfoliosRepository.findByUserId(userId);
    }
    
    // DODAJ nową pozycję do portfolio
    @PostMapping
    public Portfolios addPortfolio(@RequestBody Portfolios portfolio) {
    portfolio.setId(null); // upewnij się, że to INSERT, nie UPDATE
    portfolio.setLastUpdated(java.time.LocalDateTime.now());
    return portfoliosRepository.save(portfolio);
}
    
    // Aktualizuj ilość akcji
    @PutMapping("/{id}")
    public Portfolios updatePortfolio(@PathVariable Long id, @RequestBody Portfolios portfolioDetails) {
        Portfolios portfolio = portfoliosRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        
        portfolio.setQuantity(portfolioDetails.getQuantity());
        portfolio.setLastUpdated(java.time.LocalDateTime.now());
        
        return portfoliosRepository.save(portfolio);
    }
    
    // Usuń pozycję z portfolio
    @DeleteMapping("/{id}")
    public void deletePortfolio(@PathVariable Long id) {
        portfoliosRepository.deleteById(id);
    }
}