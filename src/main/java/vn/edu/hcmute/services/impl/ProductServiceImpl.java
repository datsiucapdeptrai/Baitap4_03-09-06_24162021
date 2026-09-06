package vn.edu.hcmute.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import vn.edu.hcmute.dao.ProductDao;
import vn.edu.hcmute.dao.impl.ProductDaoImpl;
import vn.edu.hcmute.models.Product;
import vn.edu.hcmute.services.ProductService;

public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao =
            new ProductDaoImpl();

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public List<Product> findLatest(int limit) {
        if (limit <= 0) {
            limit = 10;
        }

        return productDao.findLatest(limit);
    }

    @Override
    public List<Product> findPage(
            int page,
            int pageSize) {

        if (page < 1) {
            page = 1;
        }

        if (pageSize <= 0) {
            pageSize = 6;
        }

        /*
         * Bên giao diện trang bắt đầu từ 1,
         * còn setFirstResult của JPA bắt đầu từ 0.
         */
        return productDao.findPage(
                page - 1,
                pageSize
        );
    }

    @Override
    public Product findById(Long id) {
        if (id == null) {
            return null;
        }

        return productDao.findById(id);
    }

    @Override
    public int getTotalPages(int pageSize) {
        if (pageSize <= 0) {
            pageSize = 6;
        }

        long totalProducts = productDao.count();

        return (int) Math.ceil(
                (double) totalProducts / pageSize
        );
    }

    @Override
    public void create(Product product) {
        if (product == null) {
            throw new IllegalArgumentException(
                    "Thông tin sản phẩm không hợp lệ."
            );
        }

        LocalDateTime now = LocalDateTime.now();

        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        productDao.insert(product);
    }

    @Override
    public void update(Product product) {
        if (product == null ||
                product.getProductId() == null) {

            throw new IllegalArgumentException(
                    "Thông tin sản phẩm không hợp lệ."
            );
        }

        Product oldProduct =
                productDao.findById(
                        product.getProductId()
                );

        if (oldProduct == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy sản phẩm cần cập nhật."
            );
        }

        /*
         * Không tải ảnh mới thì giữ ảnh cũ.
         */
        if (product.getImages() == null ||
                product.getImages().isBlank()) {

            product.setImages(
                    oldProduct.getImages()
            );
        }

        product.setUpdatedAt(
                LocalDateTime.now()
        );

        productDao.update(product);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "ID sản phẩm không hợp lệ."
            );
        }

        productDao.delete(id);
    }
}