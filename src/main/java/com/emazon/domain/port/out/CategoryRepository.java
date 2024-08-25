package com.emazon.domain.port.out;

import com.emazon.domain.model.Category;

import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findByName(String name);
    Category save(Category category);
}
