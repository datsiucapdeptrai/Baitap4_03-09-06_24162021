package vn.edu.hcmute.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class FormValidator {
    private static final Pattern FULL_NAME =
            Pattern.compile("^[\\p{L} .'-]{3,100}$");
    private static final Pattern PHONE =
            Pattern.compile("^(0\\d{9}|\\+84\\d{9})$");

    private FormValidator() {
    }

    public static String clean(String value) {
        return value == null ? "" : value.trim();
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
