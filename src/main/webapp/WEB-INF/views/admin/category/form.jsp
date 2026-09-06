<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>
        ${mode == 'edit'
            ? 'Cập nhật danh mục'
            : 'Thêm danh mục'}
    </title>
</head>
<body>

<c:choose>
    <c:when test="${mode == 'edit'}">
        <c:url value="/admin/category/edit"
               var="formAction"/>
    </c:when>

    <c:otherwise>
        <c:url value="/admin/category/add"
               var="formAction"/>
    </c:otherwise>
</c:choose>

<c:url value="/admin/category/list"
       var="listUrl"/>

<div class="row justify-content-center">
    <div class="col-lg-7">

        <div class="card shadow-sm">
            <div class="card-body p-4">

                <h1 class="h3 mb-4">
                    ${mode == 'edit'
                        ? 'Cập nhật danh mục'
                        : 'Thêm danh mục'}
                </h1>

                <c:if test="${not empty errors.general}">
                    <div class="alert alert-danger">
                        <c:out value="${errors.general}"/>
                    </div>
                </c:if>

                <form method="post"
                      action="${formAction}"
                      novalidate>

                    <c:if test="${mode == 'edit'}">
                        <input type="hidden"
                               name="id"
                               value="${category.categoryId}">
                    </c:if>

                    <div class="mb-3">
                        <label for="categoryName"
                               class="form-label">
                            Tên danh mục
                            <span class="text-danger">*</span>
                        </label>

                        <input type="text"
                               id="categoryName"
                               name="categoryName"
                               maxlength="100"
                               required
                               value="<c:out value='${category.categoryName}'/>"
                               class="form-control
                                   ${not empty errors.categoryName
                                     ? 'is-invalid'
                                     : ''}">

                        <c:if test="${not empty errors.categoryName}">
                            <div class="invalid-feedback">
                                <c:out value="${errors.categoryName}"/>
                            </div>
                        </c:if>
                    </div>

                    <div class="mb-4">
                        <label for="status"
                               class="form-label">
                            Trạng thái
                        </label>

                        <select id="status"
                                name="status"
                                class="form-select
                                    ${not empty errors.status
                                      ? 'is-invalid'
                                      : ''}">

                            <option value="1"
                                ${category.status == 1
                                  ? 'selected'
                                  : ''}>
                                Hoạt động
                            </option>

                            <option value="0"
                                ${category.status == 0
                                  ? 'selected'
                                  : ''}>
                                Tạm khóa
                            </option>
                        </select>

                        <c:if test="${not empty errors.status}">
                            <div class="invalid-feedback">
                                <c:out value="${errors.status}"/>
                            </div>
                        </c:if>
                    </div>

                    <button type="submit"
                            class="btn btn-primary">
                        ${mode == 'edit'
                            ? 'Lưu thay đổi'
                            : 'Thêm danh mục'}
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