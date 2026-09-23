package com.xintong.staff.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xintong.staff.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(String name, String code);
}
