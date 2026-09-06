package vn.edu.hcmute.dao;

import java.util.List;
import vn.edu.hcmute.models.Product;

public interface ProductDao {

    List<Product> findAll();

    List<Product> findLatest(int limit);

    List<Product> findPage(int pageIndex, int pageSize);

    Product findById(Long id);

    long count();

    void insert(Product product);

    void update(Product product);

    void delete(Long id);
}