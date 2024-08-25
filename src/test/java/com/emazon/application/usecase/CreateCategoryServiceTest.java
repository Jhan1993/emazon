
package com.emazon.application.usecase;

import com.emazon.domain.model.Category;
import com.emazon.domain.port.out.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CreateCategoryService createCategoryService;

    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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

    private static Stream<Arguments> provideInvalidCategories() {
        return Stream.of(
                Arguments.of(new Category(null, "", "Valid description"), "name"),
                Arguments.of(new Category(null, "Este nombre de categoría es demasiado largo y excede los cincuenta caracteres permitidos", "Valid description"), "name"),
                Arguments.of(new Category(null, "Valid name", ""), "description"),
                Arguments.of(new Category(null, "Valid name", "Esta descripción de categoría es demasiado larga y excede los noventa caracteres permitidos por las reglas de validación"), "description")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCategories")
    void createCategory_shouldFailValidation_whenInvalidCategoryProvided(Category category, String expectedInvalidField) {
        // When
        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(expectedInvalidField)));
    }
}
