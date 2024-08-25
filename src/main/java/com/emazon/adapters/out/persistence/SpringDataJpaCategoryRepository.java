package com.emazon.adapters.out.persistence;

import com.emazon.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataJpaCategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}