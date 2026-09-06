package vn.edu.hcmute.controllers;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

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

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserAccountDao userAccountDao =
            new UserAccountDao();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        /*
         * Nếu đã đăng nhập mà tiếp tục truy cập /login
         * thì chuyển về trang chủ.
         */
        if (session != null &&
                session.getAttribute("account") != null) {

            response.sendRedirect(
                    request.getContextPath() + "/home"
            );

            return;
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/login.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String loginName =
                FormValidator.clean(
                        request.getParameter("loginName")
                );

        String password =
                request.getParameter("password");

        Map<String, String> errors =
                FormValidator.validateLogin(
                        loginName,
                        password
                );

        /*
         * Kiểm tra dữ liệu nhập trên form.
         */
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("loginName", loginName);

            request.getRequestDispatcher(
                    "/WEB-INF/views/login.jsp"
            ).forward(request, response);

            return;
        }

        Optional<UserAccount> result =
                userAccountDao.findByUsernameOrEmail(
                        loginName
                );

        /*
         * Kiểm tra tài khoản và mật khẩu.
         */
        if (result.isEmpty() ||
                !PasswordUtil.verify(
                        password,
                        result.get().getPasswordHash()
                )) {

            request.setAttribute(
                    "error",
                    "Tên đăng nhập/email hoặc mật khẩu không đúng."
            );

            request.setAttribute(
                    "loginName",
                    loginName
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/login.jsp"
            ).forward(request, response);

            return;
        }

        UserAccount account = result.get();

        /*
         * Kiểm tra trạng thái tài khoản.
         */
        if (!account.isEnabled()) {
            request.setAttribute(
                    "error",
                    "Tài khoản đang bị khóa."
            );

            request.setAttribute(
                    "loginName",
                    loginName
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/login.jsp"
            ).forward(request, response);

            return;
        }

        /*
         * Lấy URL cần quay lại trước khi hủy session cũ.
         */
        HttpSession oldSession =
                request.getSession(false);

        String returnUrl = null;

        if (oldSession != null) {
            Object savedUrl =
                    oldSession.getAttribute(
                            "returnAfterLogin"
                    );

            if (savedUrl instanceof String) {
                returnUrl = (String) savedUrl;
            }

            /*
             * Chống session fixation:
             * đăng nhập thành công thì hủy session cũ.
             */
            oldSession.invalidate();
        }

        /*
         * Chỉ chấp nhận đường dẫn nội bộ của website.
         */
        if (returnUrl == null ||
                returnUrl.isBlank() ||
                !returnUrl.startsWith("/") ||
                returnUrl.startsWith("//")) {

            returnUrl = "/home";
        }

        /*
         * Tạo session mới sau khi đăng nhập.
         */
        HttpSession newSession =
                request.getSession(true);

        newSession.setAttribute(
                "account",
                account
        );

        // Session hết hạn sau 30 phút không hoạt động.
        newSession.setMaxInactiveInterval(30 * 60);

        /*
         * Nếu người dùng đăng nhập trực tiếp:
         * chuyển đến /home.
         *
         * Nếu người dùng bị AuthFilter chuyển đến login:
         * quay lại trang trước đó.
         */
        response.sendRedirect(
                request.getContextPath() + returnUrl
        );
    }
}