package com.stockexchange.stock_exchange_backend.controller;

import com.stockexchange.stock_exchange_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.stockexchange.stock_exchange_backend.model.Users;

import java.util.List;

@RestController
@RequestMapping("/api/users")  // Wszystkie endpointy zaczynają się od /api/users
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/fullnames")
    public List<String> getAllFullNames() {
        return userRepository.findAllFullNames();
    }
    
    @GetMapping
    public List <Users> getAllUsers(){
        return userRepository.findAllUsers();
    }


}