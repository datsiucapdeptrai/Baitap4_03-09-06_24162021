package vn.edu.hcmute.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class FormValidator {
	private static final Pattern FULL_NAME = Pattern.compile("^[\\p{L} .'-]{3,100}$");
	private static final Pattern PHONE = Pattern.compile("^(0\\d{9}|\\+84\\d{9})$");

	private FormValidator() {
	}

	public static String clean(String value) {
		return value == null ? "" : value.trim();
	}

	public static Map<String, String> validateProduct(String productName, String description, String priceValue,
			String quantityValue, String categoryIdValue) {

		Map<String, String> errors = new HashMap<>();

		String name = clean(productName);
		String productDescription = clean(description);
		String priceText = clean(priceValue);
		String quantityText = clean(quantityValue);
		String categoryText = clean(categoryIdValue);

		/*
		 * Kiểm tra tên sản phẩm.
		 */
		if (name.isEmpty()) {
			errors.put("productName", "Tên sản phẩm không được để trống.");

		} else if (name.length() < 2 || name.length() > 150) {

			errors.put("productName", "Tên sản phẩm phải có từ 2 đến 150 ký tự.");
		}

		/*
		 * Kiểm tra mô tả.
		 */
		if (productDescription.length() > 2000) {
			errors.put("description", "Mô tả không được vượt quá 2000 ký tự.");
		}

		/*
		 * Kiểm tra giá bán.
		 */
		if (priceText.isEmpty()) {
			errors.put("price", "Giá sản phẩm không được để trống.");

		} else {
			try {
				BigDecimal price = new BigDecimal(priceText);

			

				BigDecimal maximumPrice =
				        new BigDecimal("9999999999999999.99");

				if (price.compareTo(BigDecimal.ZERO) <= 0) {
				    errors.put(
				            "price",
				            "Giá sản phẩm phải lớn hơn 0."
				    );

				} else if (price.compareTo(maximumPrice) > 0) {
				    errors.put(
				            "price",
				            "Giá sản phẩm vượt quá giới hạn cho phép."
				    );
				}

			} catch (NumberFormatException e) {
				errors.put("price", "Giá sản phẩm không hợp lệ.");
			}
		}

		/*
		 * Kiểm tra số lượng.
		 */
		if (quantityText.isEmpty()) {
			errors.put("quantity", "Số lượng không được để trống.");

		} else {
			try {
				int quantity = Integer.parseInt(quantityText);

				if (quantity < 0) {
					errors.put("quantity", "Số lượng không được nhỏ hơn 0.");
				}

			} catch (NumberFormatException e) {
				errors.put("quantity", "Số lượng phải là số nguyên.");
			}
		}

		/*
		 * Kiểm tra Category.
		 */
		if (categoryText.isEmpty()) {
			errors.put("categoryId", "Bạn chưa chọn danh mục.");

		} else {
			try {
				long categoryId = Long.parseLong(categoryText);

				if (categoryId <= 0) {
					errors.put("categoryId", "Danh mục không hợp lệ.");
				}

			} catch (NumberFormatException e) {
				errors.put("categoryId", "Danh mục không hợp lệ.");
			}
		}

		return errors;
	}

	public static Map<String, String> validateLogin(String loginName, String password) {
		Map<String, String> errors = new LinkedHashMap<>();
		if (loginName.isBlank()) {
			errors.put("loginName", "Tên đăng nhập hoặc email không được để trống.");
		} else if (loginName.length() > 255) {
			errors.put("loginName", "Tên đăng nhập hoặc email quá dài.");
		}
		if (password == null || password.isBlank()) {
			errors.put("password", "Mật khẩu không được để trống.");
		} else if (password.length() > 200) {
			errors.put("password", "Mật khẩu quá dài.");
		}
		return errors;
	}

	public static Map<String, String> validateCategory(String categoryName, String statusValue) {

		Map<String, String> errors = new HashMap<>();

		String name = categoryName == null ? "" : categoryName.trim();

		String status = statusValue == null ? "" : statusValue.trim();

		if (name.isEmpty()) {
			errors.put("categoryName", "Tên danh mục không được để trống.");
		} else if (name.length() < 2 || name.length() > 100) {
			errors.put("categoryName", "Tên danh mục phải có từ 2 đến 100 ký tự.");
		}

		if (!status.equals("0") && !status.equals("1")) {
			errors.put("status", "Trạng thái không hợp lệ.");
		}

		return errors;
	}

	public static Map<String, String> validateProfile(String fullName, String phone) {
		Map<String, String> errors = new LinkedHashMap<>();
		if (fullName.isBlank()) {
			errors.put("fullName", "Họ tên không được để trống.");
		} else if (!FULL_NAME.matcher(fullName).matches()) {
			errors.put("fullName", "Họ tên phải có 3-100 ký tự và không chứa số.");
		}

		if (phone.isBlank()) {
			errors.put("phone", "Số điện thoại không được để trống.");
		} else if (!PHONE.matcher(phone).matches()) {
			errors.put("phone", "Nhập dạng 0xxxxxxxxx hoặc +84xxxxxxxxx.");
		}
		return errors;
	}
}
