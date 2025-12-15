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
}
