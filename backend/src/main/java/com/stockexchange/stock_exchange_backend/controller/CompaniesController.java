package com.stockexchange.stock_exchange_backend.controller;

import com.stockexchange.stock_exchange_backend.model.Companies;
import com.stockexchange.stock_exchange_backend.repository.CompaniesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "http://localhost:4200")
public class CompaniesController {
    
    @Autowired
    private CompaniesRepository companiesRepository;
    
    // Pobierz wszystkie spółki
    @GetMapping
    public List<String> getAllCompanies() {
        return companiesRepository.FindAll();
    }
    
    // // Pobierz spółkę po ID
    // @GetMapping("/{id}")
    // public Companies getCompanyById(@PathVariable Long id) {
    //     return companiesRepository.findById(id).orElse(null);
    // }
    
    // // Pobierz spółkę po symbolu
    // @GetMapping("/symbol/{symbol}")
    // public Companies getCompanyBySymbol(@PathVariable String symbol) {
    //     return companiesRepository.findBySymbol(symbol);
    // }
    
    // // Dodaj nową spółkę
    // @PostMapping
    // public Companies addCompany(@RequestBody Companies company) {
    //     return companiesRepository.save(company);
    // }
}