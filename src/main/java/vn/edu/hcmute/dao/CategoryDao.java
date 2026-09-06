package vn.edu.hcmute.dao;

import java.util.List;

import vn.edu.hcmute.models.Category;

public interface CategoryDao {

    List<Category> findAll();

    Category findById(Long id);

    boolean existsByName(String categoryName, Long ignoredId);

    long countProducts(Long categoryId);

    void insert(Category category);

    void update(Category category);

    void delete(Long id);
}