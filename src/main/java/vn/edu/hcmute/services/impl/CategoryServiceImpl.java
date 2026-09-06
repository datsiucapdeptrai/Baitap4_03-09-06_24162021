package vn.edu.hcmute.services.impl;

import java.util.List;

import vn.edu.hcmute.dao.CategoryDao;
import vn.edu.hcmute.dao.impl.CategoryDaoImpl;
import vn.edu.hcmute.models.Category;
import vn.edu.hcmute.services.CategoryService;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryDao.findById(id);
    }

    @Override
    public void create(String categoryName, Integer status) {
        String normalizedName = categoryName.trim();

        if (categoryDao.existsByName(normalizedName, null)) {
            throw new IllegalArgumentException(
                    "Tên danh mục đã tồn tại."
            );
        }

        Category category = new Category();
        category.setCategoryName(normalizedName);
        category.setStatus(status);
        category.setImages(null);

        categoryDao.insert(category);
    }

    @Override
    public void update(Long id, String categoryName, Integer status) {
        Category existingCategory = categoryDao.findById(id);

        if (existingCategory == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy danh mục cần cập nhật."
            );
        }

        String normalizedName = categoryName.trim();

        if (categoryDao.existsByName(normalizedName, id)) {
            throw new IllegalArgumentException(
                    "Tên danh mục đã tồn tại."
            );
        }

        existingCategory.setCategoryName(normalizedName);
        existingCategory.setStatus(status);

        categoryDao.update(existingCategory);
    }

    @Override
    public void delete(Long id) {
        Category category = categoryDao.findById(id);

        if (category == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy danh mục cần xóa."
            );
        }

        long numberOfProducts = categoryDao.countProducts(id);

        if (numberOfProducts > 0) {
            throw new IllegalStateException(
                    "Không thể xóa danh mục vì đang có sản phẩm thuộc danh mục này."
            );
        }

        categoryDao.delete(id);
    }
}