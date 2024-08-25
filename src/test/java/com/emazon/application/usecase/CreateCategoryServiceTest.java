package com.emazon.application.usecase;

import com.emazon.domain.model.Category;
import com.emazon.domain.port.out.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CreateCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CreateCategoryService createCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory_shouldSaveCategory_whenValidCategoryProvided() {
        // Given
        Category category = new Category(null, "Electrónica", "Artículos electrónicos");
        when(categoryRepository.findByName("Electrónica")).thenReturn(Optional.empty());
        when(categoryRepository.save(category)).thenReturn(new Category(1L, "Electrónica", "Artículos electrónicos"));

        // When
        Category savedCategory = createCategoryService.createCategory(category);

        // Then
        assertEquals(1L, savedCategory.getId());
        assertEquals("Electrónica", savedCategory.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void createCategory_shouldThrowException_whenCategoryNameAlreadyExists() {
        // Given
        Category category = new Category(null, "Electrónica", "Artículos electrónicos");
        when(categoryRepository.findByName("Electrónica")).thenReturn(Optional.of(category));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> createCategoryService.createCategory(category));
        verify(categoryRepository, never()).save(any(Category.class));
    }
}
