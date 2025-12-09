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
    public List<Companies> getAllCompanies() {
        return companiesRepository.findAllCompanies();
    }
    
    @GetMapping("/with-category")
    public List<CompanyWithCategory> getAllCompaniesWithCategory() {
    return companiesRepository.findAllWithCategory();
}


}