package com.mystore.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mystore.app.entity.Product;


public interface ProductRepository extends JpaRepository<Product, Integer> {

    // TODO
	List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategory(String category);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Product> findByStockQuantityBetween(Integer minStock, Integer maxStock);

}
