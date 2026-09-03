package vn.edu.hcmute.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import vn.edu.hcmute.dao.UserAccountDao;
import vn.edu.hcmute.models.UserAccount;
import vn.edu.hcmute.utils.JPAUtil;
import vn.edu.hcmute.utils.PasswordUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class JPAContextListener implements ServletContextListener {
    private static final Logger LOGGER =
            Logger.getLogger(JPAContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            UserAccountDao dao = new UserAccountDao();
            if (dao.findByUsernameOrEmail("demo").isEmpty()) {
                UserAccount demo = new UserAccount();
                demo.setUsername("demo");
                demo.setEmail("demo@local.test");
                demo.setPasswordHash(PasswordUtil.hash("Demo@123"));
                demo.setFullName("Người dùng Demo");
                demo.setPhone("0901234567");
                demo.setRole("USER");
                demo.setEnabled(true);
                dao.insert(demo);
                LOGGER.info("Đã tạo tài khoản demo / Demo@123");
            }
        } catch (Throwable ex) {
            LOGGER.log(Level.SEVERE,
                    "Không thể khởi tạo JPA. Kiểm tra persistence.xml và SQL Server.", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            JPAUtil.close();
        } catch (Throwable ex) {
            LOGGER.log(Level.WARNING, "Không thể đóng EntityManagerFactory.", ex);
        }
    }
}
