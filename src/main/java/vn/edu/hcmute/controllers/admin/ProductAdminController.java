package vn.edu.hcmute.controllers.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.edu.hcmute.models.Category;
import vn.edu.hcmute.models.Product;
import vn.edu.hcmute.services.CategoryService;
import vn.edu.hcmute.services.ProductService;
import vn.edu.hcmute.services.impl.CategoryServiceImpl;
import vn.edu.hcmute.services.impl.ProductServiceImpl;
import vn.edu.hcmute.utils.FormValidator;
import vn.edu.hcmute.utils.UploadUtil;

@WebServlet("/admin/product/*")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 5 * 1024 * 1024,
    maxRequestSize = 6 * 1024 * 1024
)
public class ProductAdminController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService productService =
            new ProductServiceImpl();

    private final CategoryService categoryService =
            new CategoryServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

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
                createProduct(request, response);
                break;

            case "/edit":
                updateProduct(request, response);
                break;

            case "/delete":
                deleteProduct(request, response);
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

        moveFlash(request, "success");
        moveFlash(request, "error");

        request.setAttribute(
                "products",
                productService.findAll()
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/product/list.jsp"
        ).forward(request, response);
    }

    private void showAddForm(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Product product = new Product();
        product.setQuantity(0);

        forwardForm(
                request,
                response,
                product,
                "add",
                new HashMap<>()
        );
    }

    private void showEditForm(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Long id = parseLong(
                request.getParameter("id")
        );

        if (id == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID sản phẩm không hợp lệ."
            );
            return;
        }

        Product product =
                productService.findById(id);

        if (product == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Không tìm thấy sản phẩm."
            );
            return;
        }

        forwardForm(
                request,
                response,
                product,
                "edit",
                new HashMap<>()
        );
    }

    private void createProduct(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String productName =
                FormValidator.clean(
                        request.getParameter("productName")
                );

        String description =
                FormValidator.clean(
                        request.getParameter("description")
                );

        String priceValue =
                FormValidator.clean(
                        request.getParameter("price")
                );

        String quantityValue =
                FormValidator.clean(
                        request.getParameter("quantity")
                );

        String categoryIdValue =
                FormValidator.clean(
                        request.getParameter("categoryId")
                );

        Map<String, String> errors =
                FormValidator.validateProduct(
                        productName,
                        description,
                        priceValue,
                        quantityValue,
                        categoryIdValue
                );

        Product product = buildProduct(
                null,
                productName,
                description,
                priceValue,
                quantityValue,
                categoryIdValue
        );

        if (!errors.isEmpty()) {
            forwardForm(
                    request,
                    response,
                    product,
                    "add",
                    errors
            );
            return;
        }

        String newImage = null;

        try {
            Part imagePart =
                    request.getPart("image");

            newImage =
                    UploadUtil.saveProductImage(
                            imagePart
                    );

            if (newImage == null) {
                errors.put(
                        "image",
                        "Bạn chưa chọn ảnh sản phẩm."
                );

                forwardForm(
                        request,
                        response,
                        product,
                        "add",
                        errors
                );

                return;
            }

            product.setImages(newImage);
            System.out.println(
                    "PRICE TEXT = [" + priceValue + "]"
            );

            System.out.println(
                    "PRICE BIGDECIMAL = ["
                    + product.getPrice().toPlainString()
                    + "]"
            );
            productService.create(product);

            setFlash(
                    request,
                    "success",
                    "Thêm sản phẩm thành công."
            );

            redirectToList(request, response);

        } catch (IllegalArgumentException e) {

            UploadUtil.deleteProductImage(newImage);

            errors.put("general", e.getMessage());

            forwardForm(
                    request,
                    response,
                    product,
                    "add",
                    errors
            );

        } catch (IllegalStateException e) {

            UploadUtil.deleteProductImage(newImage);

            errors.put(
                    "image",
                    "Tệp tải lên quá lớn."
            );

            forwardForm(
                    request,
                    response,
                    product,
                    "add",
                    errors
            );
        }
    }

    private void updateProduct(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Long id = parseLong(
                request.getParameter("id")
        );

        if (id == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID sản phẩm không hợp lệ."
            );
            return;
        }

        Product existingProduct =
                productService.findById(id);

        if (existingProduct == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Không tìm thấy sản phẩm."
            );
            return;
        }

        String productName =
                FormValidator.clean(
                        request.getParameter("productName")
                );

        String description =
                FormValidator.clean(
                        request.getParameter("description")
                );

        String priceValue =
                FormValidator.clean(
                        request.getParameter("price")
                );

        String quantityValue =
                FormValidator.clean(
                        request.getParameter("quantity")
                );

        String categoryIdValue =
                FormValidator.clean(
                        request.getParameter("categoryId")
                );

        Map<String, String> errors =
                FormValidator.validateProduct(
                        productName,
                        description,
                        priceValue,
                        quantityValue,
                        categoryIdValue
                );

        Product product = buildProduct(
                id,
                productName,
                description,
                priceValue,
                quantityValue,
                categoryIdValue
        );

        product.setImages(
                existingProduct.getImages()
        );

        if (!errors.isEmpty()) {
            forwardForm(
                    request,
                    response,
                    product,
                    "edit",
                    errors
            );
            return;
        }

        String newImage = null;

        try {
            Part imagePart =
                    request.getPart("image");

            newImage =
                    UploadUtil.saveProductImage(
                            imagePart
                    );

            if (newImage != null) {
                product.setImages(newImage);
            }

            productService.update(product);

            if (newImage != null) {
                UploadUtil.deleteProductImage(
                        existingProduct.getImages()
                );
            }

            setFlash(
                    request,
                    "success",
                    "Cập nhật sản phẩm thành công."
            );

            redirectToList(request, response);

        } catch (IllegalArgumentException e) {

            UploadUtil.deleteProductImage(newImage);

            errors.put("general", e.getMessage());

            product.setImages(
                    existingProduct.getImages()
            );

            forwardForm(
                    request,
                    response,
                    product,
                    "edit",
                    errors
            );

        } catch (IllegalStateException e) {

            UploadUtil.deleteProductImage(newImage);

            errors.put(
                    "image",
                    "Tệp tải lên quá lớn."
            );

            product.setImages(
                    existingProduct.getImages()
            );

            forwardForm(
                    request,
                    response,
                    product,
                    "edit",
                    errors
            );
        }
    }

    private void deleteProduct(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        Long id = parseLong(
                request.getParameter("id")
        );

        if (id == null) {
            setFlash(
                    request,
                    "error",
                    "ID sản phẩm không hợp lệ."
            );

            redirectToList(request, response);
            return;
        }

        try {
            Product product =
                    productService.findById(id);

            if (product == null) {
                throw new IllegalArgumentException(
                        "Không tìm thấy sản phẩm."
                );
            }

            String oldImage =
                    product.getImages();

            productService.delete(id);

            UploadUtil.deleteProductImage(
                    oldImage
            );

            setFlash(
                    request,
                    "success",
                    "Xóa sản phẩm thành công."
            );

        } catch (IllegalArgumentException e) {

            setFlash(
                    request,
                    "error",
                    e.getMessage()
            );
        }

        redirectToList(request, response);
    }

    private Product buildProduct(
            Long id,
            String productName,
            String description,
            String priceValue,
            String quantityValue,
            String categoryIdValue) {

        Product product = new Product();

        product.setProductId(id);
        product.setProductName(productName);
        product.setDescription(description);

        try {
            product.setPrice(
                    new BigDecimal(priceValue)
            );
        } catch (NumberFormatException e) {
            product.setPrice(null);
        }

        try {
            product.setQuantity(
                    Integer.parseInt(quantityValue)
            );
        } catch (NumberFormatException e) {
            product.setQuantity(null);
        }

        Long categoryId =
                parseLong(categoryIdValue);

        if (categoryId != null) {
            Category category =
                    new Category();

            category.setCategoryId(categoryId);
            product.setCategory(category);
        }

        return product;
    }

    private void forwardForm(
            HttpServletRequest request,
            HttpServletResponse response,
            Product product,
            String mode,
            Map<String, String> errors)
            throws ServletException, IOException {

        request.setAttribute(
                "product",
                product
        );

        request.setAttribute(
                "categories",
                categoryService.findAll()
        );

        request.setAttribute(
                "mode",
                mode
        );

        request.setAttribute(
                "errors",
                errors
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/product/form.jsp"
        ).forward(request, response);
    }

    private Long parseLong(String value) {
        try {
            long number =
                    Long.parseLong(value);

            return number > 0
                    ? number
                    : null;

        } catch (NumberFormatException |
                 NullPointerException e) {

            return null;
        }
    }

    private void setFlash(
            HttpServletRequest request,
            String name,
            String message) {

        request.getSession()
               .setAttribute(name, message);
    }

    private void moveFlash(
            HttpServletRequest request,
            String name) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return;
        }

        Object message =
                session.getAttribute(name);

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
                        + "/admin/product/list"
        );
    }
}