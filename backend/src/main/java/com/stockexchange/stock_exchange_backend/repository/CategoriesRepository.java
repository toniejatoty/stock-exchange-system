package com.stockexchange.stock_exchange_backend.repository;

import com.stockexchange.stock_exchange_backend.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    // Users means that we will work on this type of table, Long is type of primary key
    
    @Query(value = "SELECT COUNT(*) FROM companies WHERE category_id = :categoryId", nativeQuery = true)
    Integer countCompaniesByCategoryId(Long categoryId);
    
    @Query("SELECT COUNT(c) FROM Categories c WHERE c.name = :name")
    Long countByName(String name);
}