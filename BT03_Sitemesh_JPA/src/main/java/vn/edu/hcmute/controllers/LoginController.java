package vn.edu.hcmute.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.dao.UserAccountDao;
import vn.edu.hcmute.models.UserAccount;
import vn.edu.hcmute.utils.FormValidator;
import vn.edu.hcmute.utils.PasswordUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private final UserAccountDao userAccountDao = new UserAccountDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("account") != null) {
        	response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String loginName = FormValidator.clean(request.getParameter("loginName"));
        String password = request.getParameter("password");

        Map<String, String> errors =
                FormValidator.validateLogin(loginName, password);
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("loginName", loginName);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                   .forward(request, response);
            return;
        }

        Optional<UserAccount> result =
                userAccountDao.findByUsernameOrEmail(loginName);
        if (result.isEmpty()
                || !PasswordUtil.verify(password, result.get().getPasswordHash())) {
            request.setAttribute("error",
                    "Tên đăng nhập/email hoặc mật khẩu không đúng.");
            request.setAttribute("loginName", loginName);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                   .forward(request, response);
            return;
        }

        UserAccount account = result.get();
        if (!account.isEnabled()) {
            request.setAttribute("error", "Tài khoản đang bị khóa.");
            request.setAttribute("loginName", loginName);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                   .forward(request, response);
            return;
        }

        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("account", account);
        newSession.setMaxInactiveInterval(30 * 60);
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
