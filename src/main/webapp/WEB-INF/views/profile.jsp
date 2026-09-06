<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <title>Hồ sơ cá nhân</title>
</head>

<body>

<c:url value="/profile" var="profileUrl"/>

<div class="row justify-content-center">
    <div class="col-lg-8">

        <div class="card border-0 shadow-sm">
            <div class="card-body p-4 p-md-5">

                <h1 class="h3 mb-4">
                    Hồ sơ cá nhân
                </h1>

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

                <!-- Ảnh đại diện hiện tại -->
                <div class="text-center mb-4">

                    <c:choose>

                        <c:when test="${not empty user.images}">

                            <c:url value="/uploads/users/${user.images}"
                                   var="avatarUrl"/>

                        </c:when>

                        <c:otherwise>

                            <c:url value="/assets/images/default-avatar.svg"
                                   var="avatarUrl"/>

                        </c:otherwise>

                    </c:choose>

                    <img src="${avatarUrl}"
                         id="avatarPreview"
                         class="rounded-circle border shadow-sm"
                         alt="Ảnh đại diện"
                         style="width: 150px;
                                height: 150px;
                                object-fit: cover;">

                </div>

                <!-- Form cập nhật hồ sơ -->
                <form action="${profileUrl}"
                      method="post"
                      enctype="multipart/form-data"
                      class="needs-validation"
                      novalidate>

                    <div class="row g-3">

                        <!-- Tên đăng nhập -->
                        <div class="col-md-6">

                            <label class="form-label"
                                   for="username">
                                Tên đăng nhập
                            </label>

                            <input class="form-control"
                                   id="username"
                                   type="text"
                                   value="${fn:escapeXml(user.username)}"
                                   disabled>

                        </div>

                        <!-- Email -->
                        <div class="col-md-6">

                            <label class="form-label"
                                   for="email">
                                Email
                            </label>

                            <input class="form-control"
                                   id="email"
                                   type="email"
                                   value="${fn:escapeXml(user.email)}"
                                   disabled>

                        </div>

                        <!-- Họ và tên -->
                        <div class="col-12">

                            <label class="form-label"
                                   for="fullName">

                                Họ và tên
                                <span class="text-danger">*</span>

                            </label>

                            <input id="fullName"
                                   name="fullName"
                                   type="text"
                                   minlength="3"
                                   maxlength="100"
                                   pattern="[A-Za-zÀ-ỹ .'-]{3,100}"
                                   required
                                   class="form-control${not empty errors.fullName
                                       ? ' is-invalid'
                                       : ''}"
                                   value="${fn:escapeXml(
                                       formFullName ne null
                                           ? formFullName
                                           : user.fullName
                                   )}">

                            <div class="invalid-feedback">

                                <c:choose>

                                    <c:when test="${not empty errors.fullName}">
                                        <c:out value="${errors.fullName}"/>
                                    </c:when>

                                    <c:otherwise>
                                        Vui lòng nhập họ tên hợp lệ, từ 3 đến 100 ký tự.
                                    </c:otherwise>

                                </c:choose>

                            </div>

                        </div>

                        <!-- Số điện thoại -->
                        <div class="col-12">

                            <label class="form-label"
                                   for="phone">

                                Số điện thoại
                                <span class="text-danger">*</span>

                            </label>

                            <input id="phone"
                                   name="phone"
                                   type="tel"
                                   maxlength="12"
                                   pattern="(0[0-9]{9}|[+]84[0-9]{9})"
                                   required
                                   class="form-control${not empty errors.phone
                                       ? ' is-invalid'
                                       : ''}"
                                   value="${fn:escapeXml(
                                       formPhone ne null
                                           ? formPhone
                                           : user.phone
                                   )}">

                            <div class="invalid-feedback">

                                <c:choose>

                                    <c:when test="${not empty errors.phone}">
                                        <c:out value="${errors.phone}"/>
                                    </c:when>

                                    <c:otherwise>
                                        Nhập dạng 0xxxxxxxxx hoặc +84xxxxxxxxx.
                                    </c:otherwise>

                                </c:choose>

                            </div>

                        </div>

                        <!-- Upload ảnh đại diện -->
                        <div class="col-12">

                            <label class="form-label"
                                   for="image">
                                Ảnh đại diện mới
                            </label>

                            <input id="image"
                                   name="image"
                                   type="file"
                                   accept="image/jpeg,image/png"
                                   class="form-control${not empty errors.image
                                       ? ' is-invalid'
                                       : ''}">

                            <div class="form-text">
                                Chỉ chấp nhận JPG hoặc PNG, dung lượng tối đa
                                5 MB. Nếu không muốn đổi ảnh, hãy để trống.
                            </div>

                            <c:if test="${not empty errors.image}">
                                <div class="invalid-feedback">
                                    <c:out value="${errors.image}"/>
                                </div>
                            </c:if>

                        </div>

                        <!-- Các nút -->
                        <div class="col-12 pt-2">

                            <button class="btn btn-primary"
                                    type="submit">
                                Lưu thay đổi
                            </button>

                            <a class="btn btn-outline-secondary"
                               href="${profileUrl}">
                                Hủy
                            </a>

                        </div>

                    </div>

                </form>

            </div>
        </div>

    </div>
</div>

<!-- Xem trước ảnh vừa chọn -->
<script>
    const imageInput = document.getElementById('image');
    const avatarPreview = document.getElementById('avatarPreview');

    if (imageInput && avatarPreview) {
        imageInput.addEventListener('change', function (event) {
            const file = event.target.files[0];

            if (file && file.type.startsWith('image/')) {
                avatarPreview.src = URL.createObjectURL(file);
            }
        });
    }
</script>

</body>
</html>