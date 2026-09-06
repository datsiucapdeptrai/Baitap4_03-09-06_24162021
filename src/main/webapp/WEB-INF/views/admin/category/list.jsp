<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Quản lý danh mục</title>
</head>
<body>

<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h1 class="h3 mb-1">Quản lý danh mục</h1>
        <p class="text-muted mb-0">
            Thêm, sửa và xóa danh mục sản phẩm
        </p>
    </div>

    <c:url value="/admin/category/add"
           var="addCategoryUrl"/>

    <a href="${addCategoryUrl}"
       class="btn btn-primary">
        Thêm danh mục
    </a>
</div>

<c:if test="${not empty success}">
    <div class="alert alert-success alert-dismissible fade show">
        <c:out value="${success}"/>

        <button type="button"
                class="btn-close"
                data-bs-dismiss="alert">
        </button>
    </div>
</c:if>

<c:if test="${not empty error}">
    <div class="alert alert-danger alert-dismissible fade show">
        <c:out value="${error}"/>

        <button type="button"
                class="btn-close"
                data-bs-dismiss="alert">
        </button>
    </div>
</c:if>

<div class="card shadow-sm">
    <div class="card-body">
        <div class="table-responsive">

            <table class="table table-hover align-middle">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên danh mục</th>
                    <th>Trạng thái</th>
                    <th class="text-end">Thao tác</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="category"
                           items="${categories}">

                    <tr>
                        <td>
                            <c:out value="${category.categoryId}"/>
                        </td>

                        <td>
                            <c:out value="${category.categoryName}"/>
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${category.status == 1}">
                                    <span class="badge bg-success">
                                        Hoạt động
                                    </span>
                                </c:when>

                                <c:otherwise>
                                    <span class="badge bg-secondary">
                                        Tạm khóa
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td class="text-end">
                            <c:url value="/admin/category/edit"
                                   var="editUrl">
                                <c:param name="id"
                                         value="${category.categoryId}"/>
                            </c:url>

                            <a href="${editUrl}"
                               class="btn btn-sm btn-warning">
                                Sửa
                            </a>

                            <c:url value="/admin/category/delete"
                                   var="deleteUrl"/>

                            <form method="post"
                                  action="${deleteUrl}"
                                  class="d-inline"
                                  onsubmit="return confirm(
                                      'Bạn có chắc muốn xóa danh mục này?'
                                  );">

                                <input type="hidden"
                                       name="id"
                                       value="${category.categoryId}">

                                <button type="submit"
                                        class="btn btn-sm btn-danger">
                                    Xóa
                                </button>
                            </form>
                        </td>
                    </tr>

                </c:forEach>

                <c:if test="${empty categories}">
                    <tr>
                        <td colspan="4"
                            class="text-center text-muted py-4">
                            Chưa có danh mục nào.
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