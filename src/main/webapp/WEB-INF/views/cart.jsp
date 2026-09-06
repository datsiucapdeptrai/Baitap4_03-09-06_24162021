<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <title>Giỏ hàng</title>
</head>

<body>

<c:url value="/product" var="productUrl"/>
<c:url value="/cart/clear" var="clearCartUrl"/>

<div class="d-flex justify-content-between align-items-center mb-4">

    <div>
        <h1 class="h3 mb-1">Giỏ hàng</h1>

        <p class="text-muted mb-0">
            Sản phẩm bạn đã chọn
        </p>
    </div>

    <a href="${productUrl}"
       class="btn btn-outline-primary">
        Tiếp tục mua hàng
    </a>

</div>

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

<c:choose>

    <c:when test="${empty cartItems}">

        <div class="alert alert-info text-center py-5">

            <p class="mb-3">
                Giỏ hàng hiện đang trống.
            </p>

            <a href="${productUrl}"
               class="btn btn-primary">
                Xem sản phẩm
            </a>

        </div>

    </c:when>

    <c:otherwise>

        <div class="card shadow-sm">
            <div class="card-body">

                <div class="table-responsive">

                    <table class="table table-hover align-middle">

                        <thead class="table-light">
                            <tr>
                                <th>Ảnh</th>
                                <th>Sản phẩm</th>
                                <th>Đơn giá</th>
                                <th>Số lượng</th>
                                <th>Thành tiền</th>
                                <th class="text-end">Thao tác</th>
                            </tr>
                        </thead>

                        <tbody>

                        <c:forEach var="item" items="${cartItems}">

                            <tr>

                                <td>

                                    <c:choose>

                                        <c:when test="${not empty item.product.images}">

                                            <c:url
                                                value="/product-image/${item.product.images}"
                                                var="productImageUrl"/>

                                            <img src="${productImageUrl}"
                                                 alt="Ảnh sản phẩm"
                                                 class="rounded border"
                                                 style="width: 80px;
                                                        height: 80px;
                                                        object-fit: contain;">

                                        </c:when>

                                        <c:otherwise>

                                            <div class="bg-light border rounded d-flex
                                                        align-items-center
                                                        justify-content-center
                                                        text-muted small"
                                                 style="width: 80px;
                                                        height: 80px;">
                                                Không ảnh
                                            </div>

                                        </c:otherwise>

                                    </c:choose>

                                </td>

                                <td>

                                    <strong>
                                        <c:out value="${item.product.productName}"/>
                                    </strong>

                                    <div class="small text-muted">

                                        <c:choose>

                                            <c:when test="${not empty item.product.category}">
                                                <c:out
                                                    value="${item.product.category.categoryName}"/>
                                            </c:when>

                                            <c:otherwise>
                                                Chưa có danh mục
                                            </c:otherwise>

                                        </c:choose>

                                    </div>

                                </td>

                                <td>

                                    <fmt:formatNumber
                                        value="${item.product.price}"
                                        type="number"
                                        groupingUsed="true"
                                        maxFractionDigits="0"/>
                                    ₫

                                </td>

                                <td>

                                    <span class="badge text-bg-secondary">
                                        <c:out value="${item.quantity}"/>
                                    </span>

                                </td>

                                <td class="text-danger fw-bold">

                                    <fmt:formatNumber
                                        value="${item.subtotal}"
                                        type="number"
                                        groupingUsed="true"
                                        maxFractionDigits="0"/>
                                    ₫

                                </td>

                                <td class="text-end">

                                    <c:url value="/cart/remove"
                                           var="removeUrl">

                                        <c:param
                                            name="id"
                                            value="${item.product.productId}"/>

                                    </c:url>

                                    <a href="${removeUrl}"
                                       class="btn btn-danger btn-sm"
                                       onclick="return confirm('Xóa sản phẩm này khỏi giỏ hàng?');">
                                        Xóa
                                    </a>

                                </td>

                            </tr>

                        </c:forEach>

                        </tbody>

                        <tfoot class="table-light">

                            <tr>

                                <th colspan="4"
                                    class="text-end">
                                    Tổng cộng:
                                </th>

                                <th class="text-danger fs-5">

                                    <fmt:formatNumber
                                        value="${total}"
                                        type="number"
                                        groupingUsed="true"
                                        maxFractionDigits="0"/>
                                    ₫

                                </th>

                                <th></th>

                            </tr>

                        </tfoot>

                    </table>

                </div>

                <div class="d-flex justify-content-between">

                    <a href="${clearCartUrl}"
                       class="btn btn-outline-danger"
                       onclick="return confirm('Bạn có chắc muốn xóa toàn bộ giỏ hàng?');">
                        Xóa giỏ hàng
                    </a>

                    <button type="button"
                            class="btn btn-success"
                            onclick="alert('Đặt hàng thành công trong bản demo!');">
                        Mua hàng
                    </button>

                </div>

            </div>
        </div>

    </c:otherwise>

</c:choose>

</body>
</html>