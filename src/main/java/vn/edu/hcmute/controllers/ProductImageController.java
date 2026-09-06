package vn.edu.hcmute.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmute.utils.UploadUtil;

@WebServlet("/product-image/*")
public class ProductImageController
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo =
                request.getPathInfo();

        if (pathInfo == null ||
                pathInfo.length() <= 1) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );

            return;
        }

        String fileName =
                pathInfo.substring(1);

        /*
         * Chặn truy cập ra ngoài thư mục upload.
         */
        if (fileName.contains("/") ||
                fileName.contains("\\") ||
                fileName.contains("..")) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );

            return;
        }

        Path uploadDirectory =
                UploadUtil
                        .getProductUploadDirectory();

        Path imagePath =
                uploadDirectory
                        .resolve(fileName)
                        .normalize();

        if (!imagePath.startsWith(uploadDirectory) ||
                !Files.isRegularFile(imagePath)) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );

            return;
        }

        String contentType =
                getServletContext()
                        .getMimeType(fileName);

        if (contentType == null) {
            contentType = Files.probeContentType(
                    imagePath
            );
        }

        if (contentType == null ||
                !contentType.startsWith("image/")) {

            response.sendError(
                    HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE
            );

            return;
        }

        response.setContentType(contentType);

        response.setContentLengthLong(
                Files.size(imagePath)
        );

        response.setHeader(
                "Cache-Control",
                "public, max-age=86400"
        );

        Files.copy(
                imagePath,
                response.getOutputStream()
        );
    }
}