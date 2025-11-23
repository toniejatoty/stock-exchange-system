package com.stockexchange.stock_exchange_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Aplikacja Stock Exchange działa poprawnie! ✅";
    }
    
    @GetMapping("/test")
    public String test() {
        return "Test endpoint działa! Baza danych jest podłączona.";
    }
    
    @GetMapping("/health")
    public String health() {
        return "Status: OK 🟢";
    }
}