<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <title>
        <c:out value="${product.productName}"/>
    </title>
</head>

<body>

<c:url value="/product" var="productListUrl"/>

<a href="${productListUrl}"
   class="btn btn-outline-secondary mb-4">
    Quay lại danh sách
</a>

<div class="card shadow-sm">
    <div class="card-body p-4">

        <div class="row g-5">

            <!-- Hình ảnh sản phẩm -->
            <div class="col-md-5">

                <c:choose>

                    <c:when test="${not empty product.images}">

                        <c:url value="/product-image/${product.images}"
                               var="productImageUrl"/>

                        <img src="${productImageUrl}"
                             class="img-fluid rounded w-100"
                             alt="Ảnh sản phẩm"
                             style="max-height: 450px;
                                    object-fit: contain;">

                    </c:when>

                    <c:otherwise>

                        <div class="bg-light rounded
                                    d-flex align-items-center
                                    justify-content-center
                                    text-muted"
                             style="height: 400px;">
                            Chưa có ảnh
                        </div>

                    </c:otherwise>

                </c:choose>

            </div>

            <!-- Thông tin sản phẩm -->
            <div class="col-md-7">

                <div class="text-muted mb-2">

                    Danh mục:

                    <c:choose>

                        <c:when test="${not empty product.category}">
                            <c:out value="${product.category.categoryName}"/>
                        </c:when>

                        <c:otherwise>
                            Chưa phân loại
                        </c:otherwise>

                    </c:choose>

                </div>

                <h1 class="h2 mb-3">
                    <c:out value="${product.productName}"/>
                </h1>

                <!-- Giá sản phẩm -->
                <div class="text-danger fw-bold display-6 mb-4">

                    <fmt:formatNumber value="${product.price}"
                                      type="number"
                                      groupingUsed="true"
                                      maxFractionDigits="0"/>
                    ₫

                </div>

                <!-- Số lượng sản phẩm -->
                <div class="mb-3">

                    <strong>Số lượng:</strong>

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

                <hr>

                <!-- Mô tả sản phẩm -->
                <h2 class="h5">
                    Mô tả sản phẩm
                </h2>

                <div class="text-muted mb-4"
                     style="white-space: pre-line;">

                    <c:choose>

                        <c:when test="${not empty product.description}">
                            <c:out value="${product.description}"/>
                        </c:when>

                        <c:otherwise>
                            Sản phẩm chưa có mô tả.
                        </c:otherwise>

                    </c:choose>

                </div>

                <!-- Đường dẫn thêm vào giỏ hàng -->
                <c:url value="/cart/add" var="addCartUrl">

                    <c:param name="id"
                             value="${product.productId}"/>

                </c:url>

                <c:choose>

                    <c:when test="${product.quantity > 0}">

                        <a href="${addCartUrl}"
                           class="btn btn-danger btn-lg">
                            Thêm vào giỏ hàng
                        </a>

                    </c:when>

                    <c:otherwise>

                        <button type="button"
                                class="btn btn-secondary btn-lg"
                                disabled>
                            Sản phẩm đã hết hàng
                        </button>

                    </c:otherwise>

                </c:choose>

            </div>

        </div>

    </div>
</div>

</body>
</html>