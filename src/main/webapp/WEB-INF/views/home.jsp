<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="vi">
<head>
<title>Trang chủ</title>
</head>
<body>

	<div
		class="d-flex justify-content-between
            align-items-center mb-4">

		<div>
			<h1 class="h3 mb-1">Sản phẩm mới nhất</h1>

			<p class="text-muted mb-0">10 sản phẩm vừa được cập nhật</p>
		</div>

		<c:url value="/product" var="productListUrl" />

		<a href="${productListUrl}" class="btn btn-outline-primary"> Xem
			tất cả sản phẩm </a>
	</div>

	<div class="row g-4">

		<c:forEach var="product" items="${products}">

			<div class="col-12 col-sm-6 col-lg-3">
				<div class="card h-100 shadow-sm">

					<c:choose>
						<c:when test="${not empty product.images}">

							<c:url value="/product-image/${product.images}"
								var="productImageUrl" />

							<img src="${productImageUrl}" class="card-img-top"
								alt="Ảnh sản phẩm"
								style="height: 220px; object-fit: contain; padding: 12px;">

						</c:when>

						<c:otherwise>
							<div
								class="bg-light d-flex
                                    align-items-center
                                    justify-content-center
                                    text-muted"
								style="height: 220px;">Chưa có ảnh</div>
						</c:otherwise>
					</c:choose>

					<div class="card-body d-flex flex-column">

						<div class="small text-muted mb-1">
							<c:out value="${product.category.categoryName}" />
						</div>

						<h2 class="h5 card-title">
							<c:out value="${product.productName}" />
						</h2>

						<div class="text-danger fw-bold fs-5 mb-3">
							<fmt:formatNumber value="${product.price}" type="number"
								maxFractionDigits="0" />
							₫
						</div>

						<c:url value="/product/detail" var="detailUrl">
							<c:param name="id" value="${product.productId}" />
						</c:url>

						<a href="${detailUrl}" class="btn btn-primary mt-auto"> Xem
							chi tiết </a>

					</div>
				</div>
			</div>

		</c:forEach>

		<c:if test="${empty products}">
			<div class="col-12">
				<div class="alert alert-info text-center">Chưa có sản phẩm.
					Hãy vào trang quản lý để thêm sản phẩm mới.</div>
			</div>
		</c:if>

	</div>

</body>
</html>