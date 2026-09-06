IF DB_ID(N'BT03_Sitemesh_JPA') IS NULL
BEGIN
    CREATE DATABASE BT03_Sitemesh_JPA;
END;
GO

USE BT03_Sitemesh_JPA;
GO

-- Hibernate sẽ tạo/cập nhật bảng users khi ứng dụng khởi động.
-- Sau lần chạy đầu, có thể kiểm tra bằng:
SELECT TABLE_NAME
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_TYPE = 'BASE TABLE';
GO
