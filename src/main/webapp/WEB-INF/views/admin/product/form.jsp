<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>
        ${mode eq 'edit' ? 'Cập nhật sản phẩm' : 'Thêm sản phẩm'}
    </title>
</head>

<body>

<c:choose>
    <c:when test="${mode eq 'edit'}">
        <c:url value="/admin/product/edit" var="formAction"/>
    </c:when>

    <c:otherwise>
        <c:url value="/admin/product/add" var="formAction"/>
    </c:otherwise>
</c:choose>

<c:url value="/admin/product/list" var="listUrl"/>
<c:url value="/admin/category/add" var="categoryAddUrl"/>

<div class="row justify-content-center">
    <div class="col-lg-8">

        <div class="card shadow-sm">
            <div class="card-body p-4">

                <h1 class="h3 mb-4">
                    ${mode eq 'edit'
                        ? 'Cập nhật sản phẩm'
                        : 'Thêm sản phẩm'}
                </h1>

                <c:if test="${not empty errors.general}">
                    <div class="alert alert-danger">
                        <c:out value="${errors.general}"/>
                    </div>
                </c:if>

                <c:if test="${empty categories}">
                    <div class="alert alert-warning">
                        Hiện chưa có danh mục sản phẩm.

                        <a href="${categoryAddUrl}"
                           class="alert-link">
                            Thêm danh mục trước
                        </a>
                    </div>
                </c:if>

                <form method="post"
                      action="${formAction}"
                      enctype="multipart/form-data"
                      class="needs-validation"
                      novalidate>

                    <c:if test="${mode eq 'edit'}">
                        <input type="hidden"
                               name="id"
                               value="${product.productId}">
                    </c:if>

                    <!-- Tên sản phẩm -->
                    <div class="mb-3">

                        <label for="productName"
                               class="form-label">
                            Tên sản phẩm
                            <span class="text-danger">*</span>
                        </label>

                        <input type="text"
                               id="productName"
                               name="productName"
                               maxlength="150"
                               required
                               class="form-control${not empty errors.productName
                                   ? ' is-invalid'
                                   : ''}"
                               value="${fn:escapeXml(product.productName)}">

                        <div class="invalid-feedback">
                            <c:choose>
                                <c:when test="${not empty errors.productName}">
                                    <c:out value="${errors.productName}"/>
                                </c:when>

                                <c:otherwise>
                                    Vui lòng nhập tên sản phẩm.
                                </c:otherwise>
                            </c:choose>
                        </div>

                    </div>

                    <!-- Mô tả -->
                    <div class="mb-3">

                        <label for="description"
                               class="form-label">
                            Mô tả
                        </label>

                        <textarea id="description"
                                  name="description"
                                  rows="5"
                                  maxlength="2000"
                                  class="form-control${not empty errors.description
                                      ? ' is-invalid'
                                      : ''}"><c:out value="${product.description}"/></textarea>

                        <c:if test="${not empty errors.description}">
                            <div class="invalid-feedback">
                                <c:out value="${errors.description}"/>
                            </div>
                        </c:if>

                    </div>

                    <div class="row">

                        <!-- Giá sản phẩm -->
                        <div class="col-md-6 mb-3">

                            <label for="price"
                                   class="form-label">
                                Giá sản phẩm
                                <span class="text-danger">*</span>
                            </label>

                            <input type="number"
                                   id="price"
                                   name="price"
                                   min="0.01"
                                   max="9999999999999999.99"
                                   step="0.01"
                                   required
                                   class="form-control${not empty errors.price
                                       ? ' is-invalid'
                                       : ''}"
                                   value="${product.price}">

                            <div class="invalid-feedback">
                                <c:choose>
                                    <c:when test="${not empty errors.price}">
                                        <c:out value="${errors.price}"/>
                                    </c:when>

                                    <c:otherwise>
                                        Giá phải là số lớn hơn 0.
                                    </c:otherwise>
                                </c:choose>
                            </div>

                        </div>

                        <!-- Số lượng -->
                        <div class="col-md-6 mb-3">

                            <label for="quantity"
                                   class="form-label">
                                Số lượng
                                <span class="text-danger">*</span>
                            </label>

                            <input type="number"
                                   id="quantity"
                                   name="quantity"
                                   min="0"
                                   step="1"
                                   required
                                   class="form-control${not empty errors.quantity
                                       ? ' is-invalid'
                                       : ''}"
                                   value="${product.quantity}">

                            <div class="invalid-feedback">
                                <c:choose>
                                    <c:when test="${not empty errors.quantity}">
                                        <c:out value="${errors.quantity}"/>
                                    </c:when>

                                    <c:otherwise>
                                        Số lượng phải là số nguyên từ 0 trở lên.
                                    </c:otherwise>
                                </c:choose>
                            </div>

                        </div>
                    </div>

                    <!-- Danh mục -->
                    <div class="mb-3">

                        <label for="categoryId"
                               class="form-label">
                            Danh mục
                            <span class="text-danger">*</span>
                        </label>

                        <select id="categoryId"
                                name="categoryId"
                                required
                                class="form-select${not empty errors.categoryId
                                    ? ' is-invalid'
                                    : ''}">

                            <option value="">
                                -- Chọn danh mục --
                            </option>

                            <c:forEach var="category"
                                       items="${categories}">

                                <c:choose>
                                    <c:when test="${not empty product.category
                                        and product.category.categoryId
                                            eq category.categoryId}">

                                        <option value="${category.categoryId}"
                                                selected>
                                            <c:out value="${category.categoryName}"/>
                                        </option>

                                    </c:when>

                                    <c:otherwise>

                                        <option value="${category.categoryId}">
                                            <c:out value="${category.categoryName}"/>
                                        </option>

                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>
                        </select>

                        <div class="invalid-feedback">
                            <c:choose>
                                <c:when test="${not empty errors.categoryId}">
                                    <c:out value="${errors.categoryId}"/>
                                </c:when>

                                <c:otherwise>
                                    Vui lòng chọn danh mục.
                                </c:otherwise>
                            </c:choose>
                        </div>

                    </div>

                    <!-- Upload hình ảnh -->
                    <div class="mb-4">

                        <label for="image"
                               class="form-label">
                            Hình ảnh

                            <c:if test="${mode eq 'add'}">
                                <span class="text-danger">*</span>
                            </c:if>
                        </label>

                        <c:choose>
                            <c:when test="${mode eq 'add'}">

                                <input type="file"
                                       id="image"
                                       name="image"
                                       accept="image/jpeg,image/png,image/gif,image/webp"
                                       required
                                       class="form-control${not empty errors.image
                                           ? ' is-invalid'
                                           : ''}">

                            </c:when>

                            <c:otherwise>

                                <input type="file"
                                       id="image"
                                       name="image"
                                       accept="image/jpeg,image/png,image/gif,image/webp"
                                       class="form-control${not empty errors.image
                                           ? ' is-invalid'
                                           : ''}">

                            </c:otherwise>
                        </c:choose>

                        <div class="invalid-feedback">
                            <c:choose>
                                <c:when test="${not empty errors.image}">
                                    <c:out value="${errors.image}"/>
                                </c:when>

                                <c:otherwise>
                                    Vui lòng chọn hình ảnh sản phẩm.
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="form-text">
                            Chấp nhận JPG, PNG, GIF, WEBP;
                            dung lượng tối đa 5 MB.
                        </div>

                    </div>

                    <!-- Ảnh hiện tại khi sửa sản phẩm -->
                    <c:if test="${mode eq 'edit'
                        and not empty product.images}">

                        <c:url value="/product-image/${product.images}"
                               var="currentImageUrl"/>

                        <div class="mb-4">
                            <div class="mb-2">
                                Ảnh hiện tại:
                            </div>

                            <img src="${currentImageUrl}"
                                 alt="Ảnh sản phẩm hiện tại"
                                 class="rounded border"
                                 style="width: 160px;
                                        height: 160px;
                                        object-fit: contain;">
                        </div>

                    </c:if>

                    <button type="submit"
                            class="btn btn-primary">
                        ${mode eq 'edit'
                            ? 'Lưu thay đổi'
                            : 'Thêm sản phẩm'}
                    </button>

                    <a href="${listUrl}"
                       class="btn btn-outline-secondary">
                        Quay lại
                    </a>

                </form>

            </div>
        </div>

    </div>
</div>

</body>
</html>