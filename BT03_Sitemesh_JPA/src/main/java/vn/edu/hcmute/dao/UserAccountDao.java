package vn.edu.hcmute.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.models.UserAccount;
import vn.edu.hcmute.utils.JPAUtil;

import java.util.Locale;
import java.util.Optional;

public class UserAccountDao {
    public Optional<UserAccount> findByUsernameOrEmail(String loginName) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String normalized = loginName.trim().toLowerCase(Locale.ROOT);
            return em.createQuery("""
                    SELECT u FROM UserAccount u
                    WHERE LOWER(u.username) = :loginName
                       OR LOWER(u.email) = :loginName
                    """, UserAccount.class)
                    .setParameter("loginName", normalized)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst();
        } finally {
            em.close();
        }
    }

    public UserAccount findById(long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(UserAccount.class, userId);
        } finally {
            em.close();
        }
    }

    public void insert(UserAccount user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(user);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public UserAccount updateProfile(
            long userId, String fullName, String phone, String newImageName) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            UserAccount user = em.find(UserAccount.class, userId);
            if (user == null) {
                throw new IllegalArgumentException("Không tìm thấy người dùng.");
            }

            user.setFullName(fullName);
            user.setPhone(phone);
            if (newImageName != null) {
                user.setImages(newImageName);
            }

            transaction.commit();
            return user;
        } catch (RuntimeException ex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }
}
