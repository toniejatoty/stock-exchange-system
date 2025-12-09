package com.stockexchange.stock_exchange_backend.model;
import jakarta.persistence.*;



import java.time.LocalDateTime;

@Entity
@Table(name = "USERS") // Nazwa tabeli w bazie to USERS (wielkimi literami)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
    
    // konstruktory
    public Users() {}
    
    // Poprawiony konstruktor 
    public Users(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationDate = LocalDateTime.now();
    }
    public Users(String firstName, String lastName, String email, LocalDateTime registrationDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationDate = registrationDate;
    }

    
    public Long getId() {return id;}
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { 
        this.registrationDate = registrationDate; 
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}