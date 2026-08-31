package com.sm.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sm.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
