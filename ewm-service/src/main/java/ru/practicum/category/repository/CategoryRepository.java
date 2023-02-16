package ru.practicum.category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.category.model.Category;

import java.util.ArrayList;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "select * " +
            "from  categories " +
            "GROUP BY id", nativeQuery = true)
    Page<Category> findAllCategories(Pageable pageable);

    @Query(value = "select * " +
            "from  categories " +
            "where id in ?1 " +
            "GROUP BY id", nativeQuery = true)
    ArrayList<Category> findAll(ArrayList<Integer> catIds);
}
