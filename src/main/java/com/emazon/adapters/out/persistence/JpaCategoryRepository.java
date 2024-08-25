package com.emazon.adapters.out.persistence;

import com.emazon.domain.model.Category;
import com.emazon.domain.port.out.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaCategoryRepository implements CategoryRepository {

    private final SpringDataJpaCategoryRepository springDataJpaCategoryRepository;

    @Autowired
    public JpaCategoryRepository(SpringDataJpaCategoryRepository springDataJpaCategoryRepository) {
        this.springDataJpaCategoryRepository = springDataJpaCategoryRepository;
    }

    @Override
    public Optional<Category> findByName(String name) {
        return springDataJpaCategoryRepository.findByName(name);
    }

    @Override
    public Category save(Category category) {
        return springDataJpaCategoryRepository.save(category);
    }
}
