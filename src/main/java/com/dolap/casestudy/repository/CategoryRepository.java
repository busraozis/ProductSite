package com.dolap.casestudy.repository;

import com.dolap.casestudy.model.Category;
import com.dolap.casestudy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    public List<Category> findAll();

    public Optional<Category> findById(Long id);

}
