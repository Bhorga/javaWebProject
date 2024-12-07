package com.javaWebProject.ecomproj.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaWebProject.ecomproj.model.Product;


public interface ProductRepository extends JpaRepository<Product, Integer> {
    

}
