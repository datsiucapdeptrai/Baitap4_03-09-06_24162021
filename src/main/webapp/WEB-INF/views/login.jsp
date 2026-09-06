<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>Đăng nhập</title>
</head>
<body>
	<div class="row justify-content-center py-4">
		<div class="col-md-7 col-lg-5">
			<div class="card border-0 shadow-sm">
				<div class="card-body p-4 p-md-5">
					<h1 class="h3 text-center mb-2">Đăng nhập</h1>


					<c:if test="${not empty error}">
						<div class="alert alert-danger" role="alert">
							<c:out value="${error}" />
						</div>
					</c:if>

					<c:url value="/login" var="loginUrl" />

					<form action="${loginUrl}" method="post" class="needs-validation"
						novalidate>

						<!-- Tên đăng nhập hoặc email -->
						<div class="mb-3">

							<label for="loginName" class="form-label"> Tên đăng nhập
								hoặc email </label> <input id="loginName" name="loginName" type="text"
								maxlength="255" required autofocus
								class="form-control${not empty errors.loginName
                   ? ' is-invalid'
                   : ''}"
								value="${loginName}">

							<div class="invalid-feedback">

								<c:choose>

									<c:when test="${not empty errors.loginName}">
										<c:out value="${errors.loginName}" />
									</c:when>

									<c:otherwise>
                    Vui lòng nhập tên đăng nhập hoặc email.
                </c:otherwise>

								</c:choose>

							</div>

						</div>

						<!-- Mật khẩu -->
						<div class="mb-4">

							<label for="password" class="form-label"> Mật khẩu </label> <input
								id="password" name="password" type="password" maxlength="200"
								required
								class="form-control${not empty errors.password
                   ? ' is-invalid'
                   : ''}">

							<div class="invalid-feedback">

								<c:choose>

									<c:when test="${not empty errors.password}">
										<c:out value="${errors.password}" />
									</c:when>

									<c:otherwise>
                    Vui lòng nhập mật khẩu.
                </c:otherwise>

								</c:choose>

							</div>

						</div>

						<button type="submit" class="btn btn-primary w-100">Đăng
							nhập</button>

					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
