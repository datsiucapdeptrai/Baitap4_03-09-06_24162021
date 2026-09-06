package vn.edu.hcmute.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.models.CartItem;
import vn.edu.hcmute.models.Product;
import vn.edu.hcmute.services.ProductService;
import vn.edu.hcmute.services.impl.ProductServiceImpl;

@WebServlet(
    urlPatterns = {
        "/cart",
        "/cart/*"
    }
)
public class CartController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService productService =
            new ProductServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getPathInfo();

        if (action == null ||
                action.equals("/") ||
                action.equals("/list")) {

            showCart(request, response);
            return;
        }

        switch (action) {
            case "/add":
                addToCart(request, response);
                break;

            case "/remove":
                removeFromCart(request, response);
                break;

            case "/clear":
                clearCart(request, response);
                break;

            default:
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                );
        }
    }

    private void addToCart(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        Long productId = parseId(
                request.getParameter("id")
        );

        if (productId == null) {
            setFlash(
                    request,
                    "error",
                    "ID sản phẩm không hợp lệ."
            );

            response.sendRedirect(
                    request.getContextPath() + "/product"
            );

            return;
        }

        Product product =
                productService.findById(productId);

        if (product == null) {
            setFlash(
                    request,
                    "error",
                    "Không tìm thấy sản phẩm."
            );

            response.sendRedirect(
                    request.getContextPath() + "/product"
            );

            return;
        }

        int stock = product.getQuantity() == null
                ? 0
                : product.getQuantity();

        if (stock <= 0) {
            setFlash(
                    request,
                    "error",
                    "Sản phẩm hiện đã hết hàng."
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/product/detail?id="
                            + productId
            );

            return;
        }

        HttpSession session =
                request.getSession(true);

        Map<Long, Integer> cart =
                getCart(session);

        int currentQuantity =
                cart.getOrDefault(productId, 0);

        if (currentQuantity >= stock) {
            setFlash(
                    request,
                    "error",
                    "Số lượng trong giỏ đã đạt số lượng tồn kho."
            );

        } else {
            cart.put(
                    productId,
                    currentQuantity + 1
            );

            setFlash(
                    request,
                    "success",
                    "Đã thêm sản phẩm vào giỏ hàng."
            );
        }

        response.sendRedirect(
                request.getContextPath() + "/cart"
        );
    }

    private void removeFromCart(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        Long productId = parseId(
                request.getParameter("id")
        );

        if (productId != null) {
            Map<Long, Integer> cart =
                    getCart(request.getSession(true));

            cart.remove(productId);

            setFlash(
                    request,
                    "success",
                    "Đã xóa sản phẩm khỏi giỏ hàng."
            );
        }

        response.sendRedirect(
                request.getContextPath() + "/cart"
        );
    }

    private void clearCart(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        getCart(
                request.getSession(true)
        ).clear();

        setFlash(
                request,
                "success",
                "Đã xóa toàn bộ giỏ hàng."
        );

        response.sendRedirect(
                request.getContextPath() + "/cart"
        );
    }

    private void showCart(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(true);

        Map<Long, Integer> cart =
                getCart(session);

        List<CartItem> cartItems =
                new ArrayList<>();

        List<Long> deletedProductIds =
                new ArrayList<>();

        BigDecimal total =
                BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry
                : cart.entrySet()) {

            Product product =
                    productService.findById(
                            entry.getKey()
                    );

            if (product == null) {
                deletedProductIds.add(
                        entry.getKey()
                );

                continue;
            }

            int quantity = entry.getValue();

            CartItem item =
                    new CartItem(
                            product,
                            quantity
                    );

            cartItems.add(item);

            total = total.add(
                    item.getSubtotal()
            );
        }

        for (Long deletedId : deletedProductIds) {
            cart.remove(deletedId);
        }

        moveFlash(request, "success");
        moveFlash(request, "error");

        request.setAttribute(
                "cartItems",
                cartItems
        );

        request.setAttribute(
                "total",
                total
        );

        request.setAttribute(
                "cartSize",
                cartItems.size()
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/cart.jsp"
        ).forward(request, response);
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getCart(
            HttpSession session) {

        Object savedCart =
                session.getAttribute("cart");

        if (savedCart instanceof Map<?, ?>) {
            return (Map<Long, Integer>) savedCart;
        }

        Map<Long, Integer> newCart =
                new LinkedHashMap<>();

        session.setAttribute(
                "cart",
                newCart
        );

        return newCart;
    }

    private Long parseId(String value) {
        try {
            long id = Long.parseLong(value);

            return id > 0 ? id : null;

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
}