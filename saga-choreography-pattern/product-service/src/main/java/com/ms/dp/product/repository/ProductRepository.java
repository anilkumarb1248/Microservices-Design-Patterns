package com.ms.dp.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms.dp.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
