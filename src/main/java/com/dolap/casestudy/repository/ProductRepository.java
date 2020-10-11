package com.dolap.casestudy.repository;


import com.dolap.casestudy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    public List<Product> findAll();

    public Optional<Product> findById(Long id);

    public List<Product> findByCategoryName(String categoryname);

    public List<Product> findByCategoryId(Long categoryId);

    public List<Product> findByCategoryNameContaining(String categoryname);

    public void deleteById(Long id);
}
