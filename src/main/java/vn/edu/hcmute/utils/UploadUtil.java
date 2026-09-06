package vn.edu.hcmute.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

import jakarta.servlet.http.Part;

public final class UploadUtil {

    private static final long MAX_IMAGE_SIZE =
            5L * 1024 * 1024;

    private static final Path PRODUCT_UPLOAD_DIRECTORY =
            Paths.get(
                    System.getProperty("user.home"),
                    "BT03_uploads",
                    "products"
            ).toAbsolutePath().normalize();

    private UploadUtil() {
    }

    public static Path getProductUploadDirectory() {
        return PRODUCT_UPLOAD_DIRECTORY;
    }

    public static String saveProductImage(Part imagePart)
            throws IOException {

        /*
         * Không chọn ảnh thì trả về null.
         */
        if (imagePart == null ||
                imagePart.getSize() == 0) {

            return null;
        }

        /*
         * Giới hạn dung lượng tối đa 5 MB.
         */
        if (imagePart.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException(
                    "Ảnh không được lớn hơn 5 MB."
            );
        }

        String contentType =
                imagePart.getContentType();

        String extension =
                getAllowedExtension(contentType);

        /*
         * Tạo thư mục nếu chưa tồn tại.
         */
        Files.createDirectories(
                PRODUCT_UPLOAD_DIRECTORY
        );

        /*
         * Đổi tên ảnh bằng UUID để không bị trùng.
         */
        String generatedFileName =
                UUID.randomUUID() + extension;

        Path targetFile =
                PRODUCT_UPLOAD_DIRECTORY
                        .resolve(generatedFileName)
                        .normalize();

        if (!targetFile.startsWith(
                PRODUCT_UPLOAD_DIRECTORY)) {

            throw new IllegalArgumentException(
                    "Tên tệp ảnh không hợp lệ."
            );
        }

        try (InputStream inputStream =
                     imagePart.getInputStream()) {

            Files.copy(
                    inputStream,
                    targetFile,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }

        return generatedFileName;
    }

    public static void deleteProductImage(
            String fileName) {

        if (fileName == null ||
                fileName.isBlank()) {

            return;
        }

        /*
         * Không chấp nhận đường dẫn thư mục.
         */
        if (fileName.contains("/") ||
                fileName.contains("\\") ||
                fileName.contains("..")) {

            return;
        }

        Path imagePath =
                PRODUCT_UPLOAD_DIRECTORY
                        .resolve(fileName)
                        .normalize();

        if (!imagePath.startsWith(
                PRODUCT_UPLOAD_DIRECTORY)) {

            return;
        }

        try {
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            /*
             * Không làm hỏng thao tác xóa Product
             * nếu việc xóa ảnh thất bại.
             */
            e.printStackTrace();
        }
    }

    private static String getAllowedExtension(
            String contentType) {

        String normalizedType =
                contentType == null
                        ? ""
                        : contentType.toLowerCase(
                                Locale.ROOT
                        );

        return switch (normalizedType) {
            case "image/jpeg", "image/jpg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";

            default -> throw new IllegalArgumentException(
                    "Chỉ chấp nhận ảnh JPG, PNG, GIF hoặc WEBP."
            );
        };
    }

	public static String validateImage(Part imagePart) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String saveUserImage(Part imagePart) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void deleteUserImage(String oldImageName) {
		// TODO Auto-generated method stub
		
	}

	public static Path resolveUserImage(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}
}