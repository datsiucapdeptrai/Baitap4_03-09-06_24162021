<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Hồ sơ cá nhân</title>
</head>
<body>
<div class="row justify-content-center">
    <div class="col-lg-8">
        <div class="card border-0 shadow-sm">
            <div class="card-body p-4 p-md-5">
                <h1 class="h3 mb-4">Hồ sơ cá nhân</h1>

                <c:if test="${not empty success}">
                    <div class="alert alert-success" role="alert">
                        <c:out value="${success}"/>
                    </div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        <c:out value="${error}"/>
                    </div>
                </c:if>

                <div class="text-center mb-4">
                    <c:choose>
                        <c:when test="${not empty user.images}">
                            <c:url value="/uploads/users/${user.images}" var="avatarUrl"/>
                        </c:when>
                        <c:otherwise>
                            <c:url value="/assets/images/default-avatar.svg" var="avatarUrl"/>
                        </c:otherwise>
                    </c:choose>
                    <img src="${avatarUrl}" class="avatar-preview"
                         alt="Ảnh đại diện" id="avatarPreview">
                </div>

                <form action="${pageContext.request.contextPath}/profile"
                      method="post" enctype="multipart/form-data"
                      class="needs-validation" novalidate>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label" for="username">Tên đăng nhập</label>
                            <input class="form-control" id="username" type="text"
                                   value="<c:out value='${user.username}'/>" disabled>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label" for="email">Email</label>
                            <input class="form-control" id="email" type="email"
                                   value="<c:out value='${user.email}'/>" disabled>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="fullName">Họ và tên</label>
                            <input id="fullName" name="fullName" type="text"
                                   minlength="3" maxlength="100"
                                   pattern="[A-Za-zÀ-ỹ .'-]{3,100}" required
                                   class="form-control ${not empty errors.fullName ? 'is-invalid' : ''}"
                                   value="<c:out value='${not empty formFullName ? formFullName : user.fullName}'/>" />
                            <div class="invalid-feedback">
                                <c:out value="${empty errors.fullName
                                    ? 'Vui lòng nhập họ tên hợp lệ.'
                                    : errors.fullName}"/>
                            </div>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="phone">Số điện thoại</label>
                            <input id="phone" name="phone" type="tel"
                                   maxlength="12"
                                   pattern="(0[0-9]{9}|[+]84[0-9]{9})" required
                                   class="form-control ${not empty errors.phone ? 'is-invalid' : ''}"
                                   value="<c:out value='${not empty formPhone ? formPhone : user.phone}'/>" />
                            <div class="invalid-feedback">
                                <c:out value="${empty errors.phone
                                    ? 'Nhập dạng 0xxxxxxxxx hoặc +84xxxxxxxxx.'
                                    : errors.phone}"/>
                            </div>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="image">Ảnh đại diện mới</label>
                            <input id="image" name="image" type="file"
                                   accept="image/jpeg,image/png"
                                   class="form-control ${not empty errors.image ? 'is-invalid' : ''}">
                            <div class="form-text">JPG hoặc PNG, tối đa 5 MB.</div>
                            <c:if test="${not empty errors.image}">
                                <div class="form-error"><c:out value="${errors.image}"/></div>
                            </c:if>
                        </div>

                        <div class="col-12 pt-2">
                            <button class="btn btn-primary" type="submit">Lưu thay đổi</button>
                            <a class="btn btn-outline-secondary"
                               href="${pageContext.request.contextPath}/profile">Hủy</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('image').addEventListener('change', event => {
        const file = event.target.files[0];
        if (file && file.type.startsWith('image/')) {
            document.getElementById('avatarPreview').src = URL.createObjectURL(file);
        }
    });
</script>
</body>
</html>
