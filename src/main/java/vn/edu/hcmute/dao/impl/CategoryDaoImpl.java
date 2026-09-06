package vn.edu.hcmute.dao.impl;

import java.util.List;
import java.util.Locale;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.dao.CategoryDao;
import vn.edu.hcmute.models.Category;
import vn.edu.hcmute.utils.JPAUtil;

public class CategoryDaoImpl implements CategoryDao {

    @Override
    public List<Category> findAll() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT c FROM Category c ORDER BY c.categoryId DESC",
                    Category.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Category findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsByName(String categoryName, Long ignoredId) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            String normalizedName = categoryName
                    .trim()
                    .toLowerCase(Locale.ROOT);

            String jpql =
                    "SELECT COUNT(c) FROM Category c " +
                    "WHERE LOWER(TRIM(c.categoryName)) = :categoryName";

            if (ignoredId != null) {
                jpql += " AND c.categoryId <> :ignoredId";
            }

            var query = em.createQuery(jpql, Long.class);
            query.setParameter("categoryName", normalizedName);

            if (ignoredId != null) {
                query.setParameter("ignoredId", ignoredId);
            }

            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public long countProducts(Long categoryId) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT COUNT(p) FROM Product p " +
                    "WHERE p.category.categoryId = :categoryId",
                    Long.class
            ).setParameter("categoryId", categoryId)
             .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public void insert(Category category) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(category);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Category category) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Category managedCategory =
                    em.find(Category.class, category.getCategoryId());

            if (managedCategory == null) {
                throw new IllegalArgumentException(
                        "Không tìm thấy danh mục cần cập nhật."
                );
            }

            managedCategory.setCategoryName(category.getCategoryName());
            managedCategory.setStatus(category.getStatus());

            // Chưa sửa ảnh ở phần này nên giữ nguyên images.

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
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

            Category category = em.find(Category.class, id);

            if (category != null) {
                em.remove(category);
            }

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        } finally {
            em.close();
        }
    }
}