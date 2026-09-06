<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <title>Tất cả sản phẩm</title>
</head>

<body>

<div class="mb-4">

    <h1 class="h3 mb-1">
        Tất cả sản phẩm
    </h1>

    <p class="text-muted mb-0">
        Hiển thị 6 sản phẩm trên mỗi trang
    </p>

</div>

<div class="row g-4">

    <c:forEach var="product" items="${products}">

        <div class="col-12 col-sm-6 col-lg-4">

            <div class="card h-100 shadow-sm">

                <!-- Hình ảnh sản phẩm -->
                <c:choose>

                    <c:when test="${not empty product.images}">

                        <c:url value="/product-image/${product.images}"
                               var="productImageUrl"/>

                        <img src="${productImageUrl}"
                             class="card-img-top"
                             alt="Ảnh sản phẩm"
                             style="height: 240px;
                                    object-fit: contain;
                                    padding: 12px;">

                    </c:when>

                    <c:otherwise>

                        <div class="bg-light
                                    d-flex
                                    justify-content-center
                                    align-items-center
                                    text-muted"
                             style="height: 240px;">
                            Chưa có ảnh
                        </div>

                    </c:otherwise>

                </c:choose>

                <div class="card-body d-flex flex-column">

                    <!-- Danh mục -->
                    <div class="small text-muted mb-2">

                        <c:choose>

                            <c:when test="${not empty product.category}">
                                <c:out value="${product.category.categoryName}"/>
                            </c:when>

                            <c:otherwise>
                                Chưa phân loại
                            </c:otherwise>

                        </c:choose>

                    </div>

                    <!-- Tên sản phẩm -->
                    <h2 class="h5 card-title">
                        <c:out value="${product.productName}"/>
                    </h2>

                    <!-- Giá sản phẩm -->
                    <div class="text-danger fw-bold fs-5 mb-2">

                        <fmt:formatNumber value="${product.price}"
                                          type="number"
                                          groupingUsed="true"
                                          maxFractionDigits="0"/>
                        ₫

                    </div>

                    <!-- Trạng thái còn hàng -->
                    <div class="mb-3">

                        <c:choose>

                            <c:when test="${product.quantity > 0}">

                                <span class="badge text-bg-success">
                                    Còn
                                    <c:out value="${product.quantity}"/>
                                    sản phẩm
                                </span>

                            </c:when>

                            <c:otherwise>

                                <span class="badge text-bg-danger">
                                    Hết hàng
                                </span>

                            </c:otherwise>

                        </c:choose>

                    </div>

                    <!-- URL chi tiết sản phẩm -->
                    <c:url value="/product/detail"
                           var="detailUrl">

                        <c:param name="id"
                                 value="${product.productId}"/>

                    </c:url>

                    <!-- URL thêm giỏ hàng -->
                    <c:url value="/cart/add"
                           var="addCartUrl">

                        <c:param name="id"
                                 value="${product.productId}"/>

                    </c:url>

                    <!-- Các nút -->
                    <div class="d-grid gap-2 mt-auto">

                        <a href="${detailUrl}"
                           class="btn btn-outline-primary">
                            Xem chi tiết
                        </a>

                        <c:choose>

                            <c:when test="${product.quantity > 0}">

                                <a href="${addCartUrl}"
                                   class="btn btn-danger">
                                    Thêm vào giỏ
                                </a>

                            </c:when>

                            <c:otherwise>

                                <button type="button"
                                        class="btn btn-secondary"
                                        disabled>
                                    Sản phẩm đã hết hàng
                                </button>

                            </c:otherwise>

                        </c:choose>

                    </div>

                </div>

            </div>

        </div>

    </c:forEach>

    <!-- Khi chưa có sản phẩm -->
    <c:if test="${empty products}">

        <div class="col-12">

            <div class="alert alert-info text-center py-4">
                Hiện tại chưa có sản phẩm nào.
            </div>

        </div>

    </c:if>

</div>

<!-- Phân trang -->
<c:if test="${totalPages > 1}">

    <nav class="mt-5"
         aria-label="Phân trang sản phẩm">

        <ul class="pagination justify-content-center">

            <!-- Trang trước -->
            <c:choose>

                <c:when test="${currentPage > 1}">

                    <c:url value="/product"
                           var="previousPageUrl">

                        <c:param name="page"
                                 value="${currentPage - 1}"/>

                    </c:url>

                    <li class="page-item">

                        <a class="page-link"
                           href="${previousPageUrl}">
                            Trước
                        </a>

                    </li>

                </c:when>

                <c:otherwise>

                    <li class="page-item disabled">

                        <span class="page-link">
                            Trước
                        </span>

                    </li>

                </c:otherwise>

            </c:choose>

            <!-- Các số trang -->
            <c:forEach begin="1"
                       end="${totalPages}"
                       var="pageNumber">

                <c:url value="/product"
                       var="pageUrl">

                    <c:param name="page"
                             value="${pageNumber}"/>

                </c:url>

                <li class="page-item${pageNumber eq currentPage
                    ? ' active'
                    : ''}">

                    <a class="page-link"
                       href="${pageUrl}">

                        <c:out value="${pageNumber}"/>

                    </a>

                </li>

            </c:forEach>

            <!-- Trang sau -->
            <c:choose>

                <c:when test="${currentPage < totalPages}">

                    <c:url value="/product"
                           var="nextPageUrl">

                        <c:param name="page"
                                 value="${currentPage + 1}"/>

                    </c:url>

                    <li class="page-item">

                        <a class="page-link"
                           href="${nextPageUrl}">
                            Sau
                        </a>

                    </li>

                </c:when>

                <c:otherwise>

                    <li class="page-item disabled">

                        <span class="page-link">
                            Sau
                        </span>

                    </li>

                </c:otherwise>

            </c:choose>

        </ul>

    </nav>

</c:if>

</body>
</html>