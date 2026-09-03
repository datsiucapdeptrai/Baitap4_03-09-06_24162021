package vn.edu.hcmute.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmute.utils.UploadUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@WebServlet("/uploads/users/*")
public class UserImageController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String pathInfo = request.getPathInfo();
        String fileName = pathInfo == null || pathInfo.length() <= 1
                ? null
                : pathInfo.substring(1);
        Path file = UploadUtil.resolveUserImage(fileName);

        if (file == null || !Files.isRegularFile(file)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = Files.probeContentType(file);
        response.setContentType(contentType == null
                ? "application/octet-stream"
                : contentType);
        response.setContentLengthLong(Files.size(file));
        response.setHeader("Cache-Control", "public, max-age=86400");
        Files.copy(file, response.getOutputStream());
    }
}
