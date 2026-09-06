package vn.edu.hcmute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.dao.ProductDao;
import vn.edu.hcmute.models.Category;
import vn.edu.hcmute.models.Product;
import vn.edu.hcmute.utils.JPAUtil;

public class ProductDaoImpl implements ProductDao {

    @Override
    public List<Product> findAll() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT p FROM Product p " +
                    "LEFT JOIN FETCH p.category " +
                    "ORDER BY p.productId DESC",
                    Product.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findLatest(int limit) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT p FROM Product p " +
                    "LEFT JOIN FETCH p.category " +
                    "ORDER BY p.createdAt DESC, p.productId DESC",
                    Product.class
            )
            .setMaxResults(limit)
            .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findPage(
            int pageIndex,
            int pageSize) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT p FROM Product p " +
                    "LEFT JOIN FETCH p.category " +
                    "ORDER BY p.createdAt DESC, p.productId DESC",
                    Product.class
            )
            .setFirstResult(pageIndex * pageSize)
            .setMaxResults(pageSize)
            .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Product findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT p FROM Product p " +
                    "LEFT JOIN FETCH p.category " +
                    "WHERE p.productId = :id",
                    Product.class
            )
            .setParameter("id", id)
            .getResultStream()
            .findFirst()
            .orElse(null);
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT COUNT(p) FROM Product p",
                    Long.class
            ).getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public void insert(Product product) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (product.getCategory() == null ||
                    product.getCategory().getCategoryId() == null) {
                throw new IllegalArgumentException(
                        "Sản phẩm chưa có danh mục."
                );
            }

            Long categoryId =
                    product.getCategory().getCategoryId();

            Category category =
                    em.find(Category.class, categoryId);

            if (category == null) {
                throw new IllegalArgumentException(
                        "Danh mục được chọn không tồn tại."
                );
            }

            product.setCategory(category);
            em.persist(product);
            transaction.commit();

        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Product product) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Product managedProduct =
                    em.find(Product.class, product.getProductId());

            if (managedProduct == null) {
                throw new IllegalArgumentException(
                        "Không tìm thấy sản phẩm cần cập nhật."
                );
            }

            if (product.getCategory() == null ||
                    product.getCategory().getCategoryId() == null) {
                throw new IllegalArgumentException(
                        "Sản phẩm chưa có danh mục."
                );
            }

            Category category = em.find(
                    Category.class,
                    product.getCategory().getCategoryId()
            );

            if (category == null) {
                throw new IllegalArgumentException(
                        "Danh mục được chọn không tồn tại."
                );
            }

            managedProduct.setProductName(
                    product.getProductName()
            );
            managedProduct.setDescription(
                    product.getDescription()
            );
            managedProduct.setPrice(
                    product.getPrice()
            );
            managedProduct.setQuantity(
                    product.getQuantity()
            );
            managedProduct.setImages(
                    product.getImages()
            );
            managedProduct.setCategory(category);
            managedProduct.setUpdatedAt(
                    product.getUpdatedAt()
            );

            transaction.commit();

        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Product product = em.find(Product.class, id);

            if (product == null) {
                throw new IllegalArgumentException(
                        "Không tìm thấy sản phẩm cần xóa."
                );
            }

            em.remove(product);
            transaction.commit();

        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        } finally {
            em.close();
        }
    }
}