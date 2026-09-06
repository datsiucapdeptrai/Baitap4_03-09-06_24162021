package vn.edu.hcmute.services;

import java.util.List;

import vn.edu.hcmute.models.Product;

public interface ProductService {

    List<Product> findAll();

    List<Product> findLatest(int limit);

    List<Product> findPage(int page, int pageSize);

    Product findById(Long id);

    int getTotalPages(int pageSize);

    void create(Product product);

    void update(Product product);

    void delete(Long id);
}