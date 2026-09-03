<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:url value="/login" var="loginUrl"/>
<c:url value="/logout" var="logoutUrl"/>
<c:url value="/profile" var="profileUrl"/>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
    <div class="container">

        <!-- Bấm BT03 sẽ về login nếu chưa đăng nhập,
             hoặc về profile nếu đã đăng nhập -->
        <a class="navbar-brand fw-bold"
           href="${empty sessionScope.account ? loginUrl : profileUrl}">
            BT03
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

        <div class="collapse navbar-collapse" id="mainNavbar">

            <!-- Đẩy toàn bộ nội dung sang góc phải -->
            <div class="ms-auto d-flex flex-wrap align-items-center gap-2">

                <c:choose>
                    <c:when test="${not empty sessionScope.account}">

                        <!-- Tên người dùng -->
                        <span class="navbar-text text-white">
                            <c:out value="${
                                empty sessionScope.account.fullName
                                ? sessionScope.account.username
                                : sessionScope.account.fullName
                            }"/>
                        </span>

                        <!-- Nút cập nhật hồ sơ nằm cạnh tên -->
                        <a class="btn btn-light btn-sm"
                           href="${profileUrl}">
                            Cập nhật hồ sơ
                        </a>

                        <!-- Nút đăng xuất -->
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