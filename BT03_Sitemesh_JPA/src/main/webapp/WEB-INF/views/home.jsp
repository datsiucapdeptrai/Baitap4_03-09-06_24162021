<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Trang chủ</title>
</head>
<body>
<section class="hero-card p-4 p-md-5 rounded-4 shadow-sm text-white mb-4">
    <div class="row align-items-center g-4">
        <div class="col-lg-8">
            <span class="badge text-bg-light text-primary mb-3">Bài tập 03</span>
            <h1 class="display-6 fw-bold">SiteMesh, Validation và User Profile</h1>
            <p class="lead mb-0">
                Một Bootstrap decorator dùng chung cho mọi content page.
            </p>
        </div>
        <div class="col-lg-4 text-lg-end">
            <c:choose>
                <c:when test="${not empty sessionScope.account}">
                    <a class="btn btn-light btn-lg"
                       href="${pageContext.request.contextPath}/profile">
                        Cập nhật hồ sơ
                    </a>
                </c:when>
                <c:otherwise>
                    <a class="btn btn-light btn-lg"
                       href="${pageContext.request.contextPath}/login">
                        Đăng nhập demo
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<div class="row g-4">
    <div class="col-md-4">
        <div class="card h-100 border-0 shadow-sm">
            <div class="card-body">
                <h2 class="h5">SiteMesh 3.2.1</h2>
                <p class="text-secondary mb-0">Header và footer chỉ viết một lần trong decorator.</p>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card h-100 border-0 shadow-sm">
            <div class="card-body">
                <h2 class="h5">Validation</h2>
                <p class="text-secondary mb-0">Kiểm tra dữ liệu ở trình duyệt và server.</p>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card h-100 border-0 shadow-sm">
            <div class="card-body">
                <h2 class="h5">JPA + Multipart</h2>
                <p class="text-secondary mb-0">Cập nhật fullname, phone và ảnh đại diện.</p>
            </div>
        </div>
    </div>
</div>

<c:if test="${empty sessionScope.account}">
    <div class="alert alert-info mt-4">
        Tài khoản mẫu: <strong>demo</strong> - Mật khẩu: <strong>Demo@123</strong>
    </div>
</c:if>
</body>
</html>
