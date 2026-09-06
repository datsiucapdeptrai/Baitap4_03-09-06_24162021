package vn.edu.hcmute.filters;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(
    filterName = "AuthFilter",
    urlPatterns = {
        "/profile",
        "/profile/*",
        "/admin/*",
        "/cart",
        "/cart/*",
        "/checkout",
        "/checkout/*"
    }
)
public class AuthFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request =
                (HttpServletRequest) servletRequest;

        HttpServletResponse response =
                (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);

        // Đã đăng nhập thì cho phép truy cập.
        if (session != null
                && session.getAttribute("account") != null) {

            chain.doFilter(request, response);
            return;
        }

        // Lưu lại URL để quay về sau khi đăng nhập.
        String contextPath = request.getContextPath();

        String returnUrl = request.getRequestURI()
                .substring(contextPath.length());

        String queryString = request.getQueryString();

        if (queryString != null && !queryString.isBlank()) {
            returnUrl += "?" + queryString;
        }

        request.getSession(true)
               .setAttribute("returnAfterLogin", returnUrl);

        response.sendRedirect(contextPath + "/login");
    }
}