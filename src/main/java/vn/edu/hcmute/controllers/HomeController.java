package vn.edu.hcmute.controllers;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmute.models.Product;
import vn.edu.hcmute.services.ProductService;
import vn.edu.hcmute.services.impl.ProductServiceImpl;

public class HomeController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService productService =
            new ProductServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Product> products =
                    productService.findLatest(10);

            request.setAttribute("products", products);

            request.getRequestDispatcher(
                    "/WEB-INF/views/home.jsp"
            ).forward(request, response);

        } catch (Exception exception) {
            throw new ServletException(
                    "Không thể tải sản phẩm mới nhất.",
                    exception
            );
        }
    }
}