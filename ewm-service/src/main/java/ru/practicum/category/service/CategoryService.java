package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.Collection;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(int catId);

    CategoryDto updateCategory(int catId, NewCategoryDto newCategoryDto);

    Collection<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategoryById(int catId);
}
