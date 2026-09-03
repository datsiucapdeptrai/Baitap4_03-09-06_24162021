package vn.edu.hcmute.utils;

import jakarta.servlet.http.Part;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public final class UploadUtil {
    public static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;
    private static final Set<String> ALLOWED_TYPES =
            Set.of("image/jpeg", "image/png");
    private static final Path USER_IMAGE_DIR = Path.of(
            System.getProperty("user.home"),
            "BT03_Sitemesh_JPA", "uploads", "users")
            .toAbsolutePath().normalize();

    private UploadUtil() {
    }

    public static String validateImage(Part part) throws IOException {
        if (part == null || part.getSize() == 0) {
            return null;
        }
        if (part.getSize() > MAX_IMAGE_SIZE) {
            return "Ảnh không được lớn hơn 5 MB.";
        }

        String contentType = part.getContentType() == null
                ? ""
                : part.getContentType().toLowerCase(Locale.ROOT);
        if (!ALLOWED_TYPES.contains(contentType)) {
            return "Chỉ chấp nhận ảnh JPG hoặc PNG.";
        }

        try (InputStream input = part.getInputStream()) {
            if (ImageIO.read(input) == null) {
                return "Nội dung file không phải ảnh hợp lệ.";
            }
        }
        return null;
    }

    public static String saveUserImage(Part part) throws IOException {
        if (part == null || part.getSize() == 0) {
            return null;
        }
        Files.createDirectories(USER_IMAGE_DIR);

        String extension = "image/png".equalsIgnoreCase(part.getContentType())
                ? ".png"
                : ".jpg";
        String fileName = UUID.randomUUID() + extension;
        Path target = USER_IMAGE_DIR.resolve(fileName).normalize();
        if (!USER_IMAGE_DIR.equals(target.getParent())) {
            throw new IOException("Đường dẫn upload không hợp lệ.");
        }

        try (InputStream input = part.getInputStream()) {
            Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return fileName;
    }

    public static Path resolveUserImage(String fileName) {
        if (fileName == null
                || !fileName.matches("[0-9a-fA-F-]+\\.(jpg|png)")) {
            return null;
        }
        Path file = USER_IMAGE_DIR.resolve(fileName).normalize();
        return USER_IMAGE_DIR.equals(file.getParent()) ? file : null;
    }

    public static void deleteUserImage(String fileName) {
        Path file = resolveUserImage(fileName);
        if (file != null) {
            try {
                Files.deleteIfExists(file);
            } catch (IOException ignored) {
                // Không làm hỏng giao dịch profile nếu ảnh cũ chưa xóa được.
            }
        }
    }
}
