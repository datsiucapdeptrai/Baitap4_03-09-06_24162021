package vn.edu.hcmute.controllers.admin;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.models.Category;
import vn.edu.hcmute.services.CategoryService;
import vn.edu.hcmute.services.impl.CategoryServiceImpl;
import vn.edu.hcmute.utils.FormValidator;

@WebServlet("/admin/category/*")
public class CategoryController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final CategoryService categoryService =
            new CategoryServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!requireLogin(request, response)) {
            return;
        }

        String action = request.getPathInfo();

        if (action == null ||
                action.equals("/") ||
                action.equals("/list")) {

            showList(request, response);
            return;
        }

        switch (action) {
            case "/add":
                showAddForm(request, response);
                break;

            case "/edit":
                showEditForm(request, response);
                break;

            default:
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!requireLogin(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action = request.getPathInfo();

        if (action == null) {
            response.sendError(
                    HttpServletResponse.SC_METHOD_NOT_ALLOWED
            );
            return;
        }

        switch (action) {
            case "/add":
                createCategory(request, response);
                break;

            case "/edit":
                updateCategory(request, response);
                break;

            case "/delete":
                deleteCategory(request, response);
                break;

            default:
                response.sendError(
                        HttpServletResponse.SC_METHOD_NOT_ALLOWED
                );
        }
    }

    private void showList(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        moveFlashMessage(request, "success");
        moveFlashMessage(request, "error");

        request.setAttribute(
                "categories",
                categoryService.findAll()
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/category/list.jsp"
        ).forward(request, response);
    }

    private void showAddForm(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Category category = new Category();
        category.setStatus(1);

        request.setAttribute("category", category);
        request.setAttribute("mode", "add");

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/category/form.jsp"
        ).forward(request, response);
    }

    private void showEditForm(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Long id = parseId(request.getParameter("id"));

        if (id == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID danh mục không hợp lệ."
            );
            return;
        }

        Category category = categoryService.findById(id);

        if (category == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Không tìm thấy danh mục."
            );
            return;
        }

        request.setAttribute("category", category);
        request.setAttribute("mode", "edit");

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/category/form.jsp"
        ).forward(request, response);
    }

    private void createCategory(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String categoryName =
                clean(request.getParameter("categoryName"));

        String statusValue =
                clean(request.getParameter("status"));

        Map<String, String> errors =
                FormValidator.validateCategory(
                        categoryName,
                        statusValue
                );

        Category formCategory = new Category();
        formCategory.setCategoryName(categoryName);
        formCategory.setStatus(
                "0".equals(statusValue) ? 0 : 1
        );

        if (!errors.isEmpty()) {
            forwardForm(
                    request,
                    response,
                    formCategory,
                    "add",
                    errors
            );
            return;
        }

        try {
            categoryService.create(
                    categoryName,
                    Integer.parseInt(statusValue)
            );

            setFlash(
                    request,
                    "success",
                    "Thêm danh mục thành công."
            );

            redirectToList(request, response);
        } catch (IllegalArgumentException e) {
            errors.put("general", e.getMessage());

            forwardForm(
                    request,
                    response,
                    formCategory,
                    "add",
                    errors
            );
        }
    }

    private void updateCategory(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Long id = parseId(request.getParameter("id"));

        String categoryName =
                clean(request.getParameter("categoryName"));

        String statusValue =
                clean(request.getParameter("status"));

        Map<String, String> errors =
                FormValidator.validateCategory(
                        categoryName,
                        statusValue
                );

        if (id == null) {
            errors.put("general", "ID danh mục không hợp lệ.");
        }

        Category formCategory = new Category();
        formCategory.setCategoryId(id);
        formCategory.setCategoryName(categoryName);
        formCategory.setStatus(
                "0".equals(statusValue) ? 0 : 1
        );

        if (!errors.isEmpty()) {
            forwardForm(
                    request,
                    response,
                    formCategory,
                    "edit",
                    errors
            );
            return;
        }

        try {
            categoryService.update(
                    id,
                    categoryName,
                    Integer.parseInt(statusValue)
            );

            setFlash(
                    request,
                    "success",
                    "Cập nhật danh mục thành công."
            );

            redirectToList(request, response);
        } catch (IllegalArgumentException e) {
            errors.put("general", e.getMessage());

            forwardForm(
                    request,
                    response,
                    formCategory,
                    "edit",
                    errors
            );
        }
    }

    private void deleteCategory(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        Long id = parseId(request.getParameter("id"));

        if (id == null) {
            setFlash(
                    request,
                    "error",
                    "ID danh mục không hợp lệ."
            );

            redirectToList(request, response);
            return;
        }

        try {
            categoryService.delete(id);

            setFlash(
                    request,
                    "success",
                    "Xóa danh mục thành công."
            );
        } catch (IllegalArgumentException |
                 IllegalStateException e) {

            setFlash(
                    request,
                    "error",
                    e.getMessage()
            );
        }

        redirectToList(request, response);
    }

    private void forwardForm(
            HttpServletRequest request,
            HttpServletResponse response,
            Category category,
            String mode,
            Map<String, String> errors)
            throws ServletException, IOException {

        request.setAttribute("category", category);
        request.setAttribute("mode", mode);
        request.setAttribute("errors", errors);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/category/form.jsp"
        ).forward(request, response);
    }

    private boolean requireLogin(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null ||
                session.getAttribute("account") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return false;
        }

        return true;
    }

    private void setFlash(
            HttpServletRequest request,
            String name,
            String message) {

        request.getSession()
               .setAttribute(name, message);
    }

    private void moveFlashMessage(
            HttpServletRequest request,
            String name) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return;
        }

        Object message = session.getAttribute(name);

        if (message != null) {
            request.setAttribute(name, message);
            session.removeAttribute(name);
        }
    }

    private void redirectToList(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/category/list"
        );
    }

    private Long parseId(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException |
                 NullPointerException e) {
            return null;
        }
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}