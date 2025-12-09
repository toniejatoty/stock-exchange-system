package com.stockexchange.stock_exchange_backend.repository;

import com.stockexchange.stock_exchange_backend.model.Companies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface CompaniesRepository extends JpaRepository<Companies, Long> {
    // @Query("SELECT c FROM Companies c WHERE c.symbol = :symbol")
    // Companies findCompanyBySymbol(@Param("symbol") String symbol);

    @Query("SELECT Concat(c.name,'-->',c.symbol) FROM Companies c")
    List <String> FindAll();
    
    @Query("Select c from Companies c")
    List<Companies> findAllCompanies();
}