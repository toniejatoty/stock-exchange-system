package com.stockexchange.stock_exchange_backend.repository;

import com.stockexchange.stock_exchange_backend.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    
    @Query("SELECT o FROM Orders o")
    List<Orders> findAllOrders();
    
    @Query(value = "SELECT COUNT(*) FROM orders WHERE user_id = :userId", nativeQuery = true)
    Integer countOrdersByUserId(@Param("userId") Long userId);
    
    @Query(value = "SELECT COUNT(*) FROM orders WHERE company_id = :companyId", nativeQuery = true)
    Integer countOrdersByCompanyId(@Param("companyId") Long companyId);
    
    @Query(value = "SELECT COUNT(*) FROM orders WHERE user_id = :userId AND status = 'PENDING'", nativeQuery = true)
    Integer countPendingOrdersByUserId(@Param("userId") Long userId);
    
    // Znajdź SELL orders dla matching z BUY order (cena sell <= cena buy)
    @Query(value = "SELECT * FROM orders WHERE company_id = :companyId AND order_type = 'SELL' AND status = 'PENDING' AND price <= :buyPrice ORDER BY price ASC, order_date ASC", nativeQuery = true)
    List<Orders> findMatchingSellOrders(@Param("companyId") Long companyId, @Param("buyPrice") java.math.BigDecimal buyPrice);
    
    // Znajdź BUY orders dla matching z SELL order (cena buy >= cena sell)
    @Query(value = "SELECT * FROM orders WHERE company_id = :companyId AND order_type = 'BUY' AND status = 'PENDING' AND price >= :sellPrice ORDER BY price DESC, order_date ASC", nativeQuery = true)
    List<Orders> findMatchingBuyOrders(@Param("companyId") Long companyId, @Param("sellPrice") java.math.BigDecimal sellPrice);
    
    // Suma ilości akcji w pending SELL orders dla użytkownika i spółki
    @Query(value = "SELECT COALESCE(SUM(quantity), 0) FROM orders WHERE user_id = :userId AND company_id = :companyId AND order_type = 'SELL' AND status = 'PENDING'", nativeQuery = true)
    Integer sumPendingSellQuantity(@Param("userId") Long userId, @Param("companyId") Long companyId);
}
