package com.example.crudweb.repository;

import com.example.crudweb.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
}
