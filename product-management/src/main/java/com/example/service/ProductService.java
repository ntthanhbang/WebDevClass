package com.example.service;

import com.example.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    
    List<Product> getAllProducts();
    
    Optional<Product> getProductById(Long id);
    
    Product saveProduct(Product product);
    
    void deleteProduct(Long id);
    
    List<String> getAllCategories();
    
    List<Product> getProductsByCategory(String category);

    long countByCategory(String category);

    BigDecimal calculateTotalValue();

    BigDecimal calculateAveragePrice();

    List<Product> findLowStockProducts(int threshold);

    Page<Product> searchProducts(String keyword, Pageable pageable);

}