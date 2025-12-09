package com.stockexchange.stock_exchange_backend.repository;

import com.stockexchange.stock_exchange_backend.model.Portfolios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;  
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PortfoliosRepository extends JpaRepository<Portfolios, Long> {

    @Query("SELECT p FROM Portfolios p WHERE p.userId = :userId")
    List<Portfolios> findByUserId(@Param("userId") Long userId);
    
    // Znajdź konkretną pozycję (user + company)
    @Query("SELECT p FROM Portfolios p WHERE p.userId = :userId AND p.companyId = :companyId")
    Portfolios findByUserIdAndCompanyId(@Param("userId") Long userId, @Param("companyId") Long companyId);
    
    // Znajdź wszystkie (bez custom query - używa wbudowanej findAll())
    // Możesz też napisać ręcznie:
    @Query("SELECT p FROM Portfolios p")
    List<Portfolios> getAllPortfolios();
}