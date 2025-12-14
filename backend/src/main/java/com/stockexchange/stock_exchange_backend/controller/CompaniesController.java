package com.stockexchange.stock_exchange_backend.controller;

import com.stockexchange.stock_exchange_backend.model.Companies;
import com.stockexchange.stock_exchange_backend.repository.CompaniesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;
import com.stockexchange.stock_exchange_backend.repository.CompaniesRepository.CompanyWithCategory; 
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "http://localhost:4200")
public class CompaniesController {
    
    @Autowired
    private CompaniesRepository companiesRepository;
    // Pobierz wszystkie spółki
    @GetMapping
    public List<Companies> getAllCompanies() {
        return companiesRepository.findAllCompanies();
    }
    
    @GetMapping("/with-category")
    public List<CompanyWithCategory> getAllCompaniesWithCategory() {
        return companiesRepository.findAllWithCategory();
    }
    
    // Dodaj nową spółkę
@PostMapping
public ResponseEntity<?> createCompany(@RequestBody Companies company) {
    // Sprawdź czy ticker LUB nazwa już istnieje
    Integer count = companiesRepository.countBySymbolOrName(
        company.getSymbol(), company.getName());
    
    if (count != null && count > 0) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body("Spółka z tickerem '" + company.getSymbol() + 
                  "' lub nazwą '" + company.getName() + "' już istnieje!");
    }
    
    Companies saved = companiesRepository.save(company);
    return ResponseEntity.ok(saved);
}
    
    // Usuń spółkę
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
    // Sprawdź czy spółka jest w portfolios
    Integer count = companiesRepository.countPortfoliosByCompanyId(id);
    
    if (count != null && count > 0) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body("Nie można usunąć spółki - jest używana w " + count + " portfolio(s)");
    }
    
    companiesRepository.deleteById(id);
    return ResponseEntity.ok().build();
}

}