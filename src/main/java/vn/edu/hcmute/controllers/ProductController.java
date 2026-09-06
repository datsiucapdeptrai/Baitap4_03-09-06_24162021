package vn.edu.hcmute.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmute.models.Product;
import vn.edu.hcmute.services.ProductService;
import vn.edu.hcmute.services.impl.ProductServiceImpl;

@WebServlet(
    urlPatterns = {
        "/product",
        "/product/*"
    }
)
public class ProductController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int PAGE_SIZE = 6;

    private final ProductService productService =
            new ProductServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        /*
         * Truy cập:
         * /product
         * /product/
         * /product/list
         */
        if (path == null ||
                path.equals("/") ||
                path.equals("/list")) {

            showProductList(request, response);
            return;
        }

        /*
         * Truy cập:
         * /product/detail?id=1
         */
        if (path.equals("/detail")) {
            showProductDetail(request, response);
            return;
        }

        response.sendError(
                HttpServletResponse.SC_NOT_FOUND
        );
    }

    private void showProductList(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int currentPage =
                parsePositiveInteger(
                        request.getParameter("page"),
                        1
                );

        int totalPages =
                productService.getTotalPages(PAGE_SIZE);

        /*
         * Không cho số trang vượt quá trang cuối.
         */
        if (totalPages > 0 &&
                currentPage > totalPages) {

            currentPage = totalPages;
        }

        request.setAttribute(
                "products",
                productService.findPage(
                        currentPage,
                        PAGE_SIZE
                )
        );

        request.setAttribute(
                "currentPage",
                currentPage
        );

        request.setAttribute(
                "totalPages",
                totalPages
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/product/list.jsp"
        ).forward(request, response);
    }

    private void showProductDetail(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Long productId =
                parsePositiveLong(
                        request.getParameter("id")
                );

        if (productId == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID sản phẩm không hợp lệ."
            );

            return;
        }

        Product product =
                productService.findById(productId);

        if (product == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Không tìm thấy sản phẩm."
            );

            return;
        }

        request.setAttribute(
                "product",
                product
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/product/detail.jsp"
        ).forward(request, response);
    }

    private int parsePositiveInteger(
            String value,
            int defaultValue) {

        try {
            int number = Integer.parseInt(value);

            return number > 0
                    ? number
                    : defaultValue;

        } catch (NumberFormatException |
                 NullPointerException e) {

            return defaultValue;
        }
    }

    private Long parsePositiveLong(String value) {
        try {
            long number = Long.parseLong(value);

            return number > 0
                    ? number
                    : null;

        } catch (NumberFormatException |
                 NullPointerException e) {

            return null;
        }
    }
}