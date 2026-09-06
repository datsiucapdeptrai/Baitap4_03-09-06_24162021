<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<c:url value="/home" var="homeUrl"/>
<c:url value="/product" var="productUrl"/>
<c:url value="/admin/category/list" var="categoryListUrl"/>
<c:url value="/login" var="loginUrl"/>
<c:url value="/logout" var="logoutUrl"/>
<c:url value="/profile" var="profileUrl"/>

<nav class="navbar navbar-expand-lg
            navbar-dark bg-primary shadow-sm">

    <div class="container">

        <!-- Logo luôn chuyển về trang chủ -->
        <a class="navbar-brand fw-bold"
           href="${homeUrl}">
            BT03 Shop
        </a>

        <button class="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#mainNavbar"
                aria-controls="mainNavbar"
                aria-expanded="false"
                aria-label="Mở menu">

            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse"
             id="mainNavbar">

            <!-- Menu bên trái -->
            <div class="navbar-nav me-auto">

                <a class="nav-link"
                   href="${homeUrl}">
                    Trang chủ
                </a>

                <a class="nav-link"
                   href="${productUrl}">
                    Sản phẩm
                </a>

                <!-- Chỉ hiện quản lý danh mục khi đã đăng nhập -->
                <c:if test="${not empty sessionScope.account}">
                    <a class="nav-link"
                       href="${categoryListUrl}">
                        Quản lý danh mục
                    </a>
                </c:if>

            </div>

            <!-- Khu vực người dùng bên phải -->
            <div class="d-flex flex-wrap
                        align-items-center gap-2">

                <c:choose>

                    <c:when test="${not empty sessionScope.account}">

                        <span class="navbar-text text-white me-1">
                            Xin chào,

                            <c:choose>
                                <c:when test="${not empty sessionScope.account.fullName}">
                                    <c:out value="${sessionScope.account.fullName}"/>
                                </c:when>

                                <c:otherwise>
                                    <c:out value="${sessionScope.account.username}"/>
                                </c:otherwise>
                            </c:choose>
                        </span>

                        <a class="btn btn-light btn-sm"
                           href="${profileUrl}">
                            Cập nhật hồ sơ
                        </a>

                        <a class="btn btn-outline-light btn-sm"
                           href="${logoutUrl}">
                            Đăng xuất
                        </a>

                    </c:when>

                    <c:otherwise>

                        <a class="btn btn-light btn-sm"
                           href="${loginUrl}">
                            Đăng nhập
                        </a>

                    </c:otherwise>

                </c:choose>

            </div>
        </div>
    </div>
</nav>