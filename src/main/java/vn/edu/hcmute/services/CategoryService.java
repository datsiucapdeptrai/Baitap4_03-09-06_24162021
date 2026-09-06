package vn.edu.hcmute.services;

import java.util.List;

import vn.edu.hcmute.models.Category;

public interface CategoryService {

    List<Category> findAll();

    Category findById(Long id);

    void create(String categoryName, Integer status);

    void update(Long id, String categoryName, Integer status);

    void delete(Long id);
}