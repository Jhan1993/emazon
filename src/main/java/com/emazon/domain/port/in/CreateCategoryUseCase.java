package com.emazon.domain.port.in;

import com.emazon.domain.model.Category;

public interface CreateCategoryUseCase {
    Category createCategory(Category category);
}