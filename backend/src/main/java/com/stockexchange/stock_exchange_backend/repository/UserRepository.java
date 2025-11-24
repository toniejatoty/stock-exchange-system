package com.stockexchange.stock_exchange_backend.repository;

import com.stockexchange.stock_exchange_backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    // Users means that we will work on this type of table, Long is type of primary key
    @Query("SELECT CONCAT(u.firstName, ' ', u.lastName) FROM Users u")
    List<String> findAllFullNames();
}