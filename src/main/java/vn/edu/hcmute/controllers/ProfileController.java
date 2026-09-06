package vn.edu.hcmute.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.edu.hcmute.dao.UserAccountDao;
import vn.edu.hcmute.models.UserAccount;
import vn.edu.hcmute.utils.FormValidator;
import vn.edu.hcmute.utils.UploadUtil;

import java.io.IOException;
import java.util.Map;

@WebServlet("/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10L * 1024 * 1024,
        maxRequestSize = 11L * 1024 * 1024
)
public class ProfileController extends HttpServlet {
    private final UserAccountDao userAccountDao = new UserAccountDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserAccount account = getLoggedInAccount(session);
        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        UserAccount user = userAccountDao.findById(account.getUserId());
        if (user == null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Object success = session.getAttribute("profileSuccess");
        if (success != null) {
            request.setAttribute("success", success);
            session.removeAttribute("profileSuccess");
        }
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserAccount account = getLoggedInAccount(session);
        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String fullName = FormValidator.clean(request.getParameter("fullName"));
        String phone = FormValidator.clean(request.getParameter("phone"));
        Map<String, String> errors =
                FormValidator.validateProfile(fullName, phone);

        Part imagePart = null;
        try {
            imagePart = request.getPart("image");
            String imageError = UploadUtil.validateImage(imagePart);
            if (imageError != null) {
                errors.put("image", imageError);
            }
        } catch (IllegalStateException ex) {
            errors.put("image", "File upload vượt quá giới hạn cho phép.");
        }

        if (!errors.isEmpty()) {
            forwardWithErrors(request, response, account,
                    fullName, phone, errors, null);
            return;
        }

        String newImageName = null;
        try {
            UserAccount current = userAccountDao.findById(account.getUserId());
            if (current == null) {
                throw new IllegalArgumentException("Không tìm thấy người dùng.");
            }

            newImageName = UploadUtil.saveUserImage(imagePart);
            String oldImageName = current.getImages();
            UserAccount updated = userAccountDao.updateProfile(
                    account.getUserId(), fullName, phone, newImageName);

            session.setAttribute("account", updated);
            session.setAttribute("profileSuccess", "Cập nhật hồ sơ thành công.");

            if (newImageName != null && oldImageName != null) {
                UploadUtil.deleteUserImage(oldImageName);
            }
            response.sendRedirect(request.getContextPath() + "/profile");
        } catch (Exception ex) {
            if (newImageName != null) {
                UploadUtil.deleteUserImage(newImageName);
            }
            forwardWithErrors(request, response, account,
                    fullName, phone, null,
                    "Không thể cập nhật hồ sơ. Kiểm tra log và thử lại.");
        }
    }

    private UserAccount getLoggedInAccount(HttpSession session) {
        return session == null
                ? null
                : (UserAccount) session.getAttribute("account");
    }

    private void forwardWithErrors(
            HttpServletRequest request,
            HttpServletResponse response,
            UserAccount account,
            String fullName,
            String phone,
            Map<String, String> errors,
            String error) throws ServletException, IOException {
        request.setAttribute("user", userAccountDao.findById(account.getUserId()));
        request.setAttribute("formFullName", fullName);
        request.setAttribute("formPhone", phone);
        request.setAttribute("errors", errors);
        request.setAttribute("error", error);
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp")
               .forward(request, response);
    }
}
