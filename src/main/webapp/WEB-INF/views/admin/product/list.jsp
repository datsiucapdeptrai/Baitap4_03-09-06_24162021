<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <title>Quản lý sản phẩm</title>
</head>

<body>

<c:url value="/admin/product/add" var="addProductUrl"/>
<c:url value="/admin/product/delete" var="deleteProductUrl"/>

<div class="d-flex justify-content-between align-items-center mb-4">

    <div>
        <h1 class="h3 mb-1">Quản lý sản phẩm</h1>

        <p class="text-muted mb-0">
            Thêm, sửa và xóa sản phẩm
        </p>
    </div>

    <a href="${addProductUrl}"
       class="btn btn-primary">
        Thêm sản phẩm
    </a>

</div>

<!-- Thông báo thành công -->
<c:if test="${not empty success}">
    <div class="alert alert-success alert-dismissible fade show"
         role="alert">

        <c:out value="${success}"/>

        <button type="button"
                class="btn-close"
                data-bs-dismiss="alert"
                aria-label="Đóng">
        </button>
    </div>
</c:if>

<!-- Thông báo lỗi -->
<c:if test="${not empty error}">
    <div class="alert alert-danger alert-dismissible fade show"
         role="alert">

        <c:out value="${error}"/>

        <button type="button"
                class="btn-close"
                data-bs-dismiss="alert"
                aria-label="Đóng">
        </button>
    </div>
</c:if>

<div class="card shadow-sm">
    <div class="card-body">

        <div class="table-responsive">

            <table class="table table-hover align-middle">

                <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Ảnh</th>
                        <th>Tên sản phẩm</th>
                        <th>Danh mục</th>
                        <th>Giá</th>
                        <th>Số lượng</th>
                        <th class="text-end">Thao tác</th>
                    </tr>
                </thead>

                <tbody>

                <c:forEach var="product" items="${products}">

                    <tr>

                        <!-- ID -->
                        <td>
                            <c:out value="${product.productId}"/>
                        </td>

                        <!-- Hình ảnh -->
                        <td>

                            <c:choose>

                                <c:when test="${not empty product.images}">

                                    <c:url value="/product-image/${product.images}"
                                           var="productImageUrl"/>

                                    <img src="${productImageUrl}"
                                         alt="${product.productName}"
                                         class="rounded border"
                                         style="width: 70px;
                                                height: 70px;
                                                object-fit: cover;">

                                </c:when>

                                <c:otherwise>

                                    <div class="bg-light border rounded
                                                d-flex align-items-center
                                                justify-content-center
                                                text-muted"
                                         style="width: 70px;
                                                height: 70px;">
                                        Không ảnh
                                    </div>

                                </c:otherwise>

                            </c:choose>

                        </td>

                        <!-- Tên sản phẩm -->
                        <td>
                            <strong>
                                <c:out value="${product.productName}"/>
                            </strong>
                        </td>

                        <!-- Danh mục -->
                        <td>

                            <c:choose>

                                <c:when test="${not empty product.category}">
                                    <c:out value="${product.category.categoryName}"/>
                                </c:when>

                                <c:otherwise>
                                    <span class="text-muted">
                                        Chưa có danh mục
                                    </span>
                                </c:otherwise>

                            </c:choose>

                        </td>

                        <!-- Giá -->
                        <td class="text-danger fw-bold">

                            <fmt:formatNumber value="${product.price}"
                                              type="number"
                                              groupingUsed="true"
                                              maxFractionDigits="0"/>
                            ₫

                        </td>

                        <!-- Số lượng -->
                        <td>

                            <c:choose>

                                <c:when test="${product.quantity > 0}">
                                    <span class="badge text-bg-success">
                                        <c:out value="${product.quantity}"/>
                                    </span>
                                </c:when>

                                <c:otherwise>
                                    <span class="badge text-bg-danger">
                                        Hết hàng
                                    </span>
                                </c:otherwise>

                            </c:choose>

                        </td>

                        <!-- Nút thao tác -->
                        <td class="text-end">

                            <c:url value="/admin/product/edit"
                                   var="editProductUrl">

                                <c:param name="id"
                                         value="${product.productId}"/>

                            </c:url>

                            <a href="${editProductUrl}"
                               class="btn btn-warning btn-sm">
                                Sửa
                            </a>

                            <form method="post"
                                  action="${deleteProductUrl}"
                                  class="d-inline"
                                  onsubmit="return confirm('Bạn có chắc muốn xóa sản phẩm này?');">

                                <input type="hidden"
                                       name="id"
                                       value="${product.productId}">

                                <button type="submit"
                                        class="btn btn-danger btn-sm">
                                    Xóa
                                </button>

                            </form>

                        </td>

                    </tr>

                </c:forEach>

                <!-- Không có sản phẩm -->
                <c:if test="${empty products}">

                    <tr>
                        <td colspan="7"
                            class="text-center text-muted py-5">

                            <div class="mb-3">
                                Hiện chưa có sản phẩm nào.
                            </div>

                            <a href="${addProductUrl}"
                               class="btn btn-primary btn-sm">
                                Thêm sản phẩm đầu tiên
                            </a>

                        </td>
                    </tr>

                </c:if>

                </tbody>

            </table>

        </div>

    </div>
</div>

</body>
</html>