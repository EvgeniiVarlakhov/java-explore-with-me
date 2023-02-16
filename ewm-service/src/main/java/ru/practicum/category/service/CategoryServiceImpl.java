package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.CategoryMapper;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Transactional
    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category newCategory = repository.save(CategoryMapper.mapToNewCategory(newCategoryDto));
        log.info("Создана новая категория: {}", newCategory);
        return CategoryMapper.toCategoryDto(newCategory);
    }

    @Transactional
    @Override
    public void deleteCategory(int catId) {
        Optional<Category> category = repository.findById(catId);
        if (category.isEmpty()) {
            throw new ObjectNotFoundException("Категории с id=" + catId + " не существует.");
        }
        repository.deleteById(catId);
        log.info("Категория с id={} удалена.", catId);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(int catId, NewCategoryDto newCategoryDto) {
        Category updateCat;
        Optional<Category> category = repository.findById(catId);
        if (category.isEmpty()) {
            throw new ObjectNotFoundException("Категории с id=" + catId + " не существует.");
        }
        updateCat = repository.save(CategoryMapper.mapToNewCategory(newCategoryDto));
        log.info("Категория id={} обновлена. {}", catId, updateCat);
        return CategoryMapper.toCategoryDto(updateCat);
    }

    @Override
    public Collection<CategoryDto> getAllCategories(int from, int size) {
        Collection<CategoryDto> categoryDtos = new ArrayList<>();
        Collection<Category> categories;
        Pageable pageable = PageRequest.of(from / size, size);
        categories = repository.findAllCategories(pageable).getContent();
        for (Category category : categories) {
            categoryDtos.add(CategoryMapper.toCategoryDto(category));
        }
        log.info("Получен список категорий: {}", categories);
        return categoryDtos;
    }

    @Override
    public CategoryDto getCategoryById(int catId) {
        Optional<Category> category = repository.findById(catId);
        if (category.isEmpty()) {
            throw new ObjectNotFoundException("Категории с id=" + catId + " не существует.");
        }
        log.info("Получена категория: {}", category.get());
        return CategoryMapper.toCategoryDto(category.get());
    }

}
