# BT03 SiteMesh JPA

Project mẫu Maven WAR cho bài tập:

1. SiteMesh Decorator 3.2.1 với một Bootstrap template.
2. Validation cho toàn bộ form đang có trong project.
3. User profile cập nhật `fullName`, `phone`, `images` bằng JPA và multipart.

## Công nghệ

- Java source level 17 (chạy được bằng JDK 17, 21 hoặc JDK 22 của bạn)
- Jakarta Servlet 6.0
- JSP/JSTL 3
- SiteMesh 3.2.1
- Hibernate ORM 7.1.35.Final
- SQL Server JDBC 13.4.0
- Tomcat 10.1
- Bootstrap 5.3.3

## Bước 1 - Chuẩn bị SQL Server

1. Mở SQL Server Configuration Manager.
2. Bật `TCP/IP` cho SQL Server instance.
3. Đặt TCP Port là `1433` nếu máy bạn chưa cấu hình.
4. Khởi động lại dịch vụ SQL Server.
5. Bật SQL Server Authentication (Mixed Mode) nếu dùng tài khoản `sa`.
6. Mở SSMS và chạy file `database/create_database.sql`.

Sau bước này phải có database:

```text
BT03_Sitemesh_JPA
```

## Bước 2 - Sửa tài khoản kết nối database

Mở:

```text
src/main/resources/META-INF/persistence.xml
```

Tìm:

```xml
<property name="jakarta.persistence.jdbc.user" value="sa"/>
<property name="jakarta.persistence.jdbc.password" value="CHANGE_ME"/>
```

Đổi `CHANGE_ME` thành mật khẩu thật của tài khoản `sa`.

Nếu SQL Server dùng port hoặc tên database khác, sửa URL:

```xml
jdbc:sqlserver://localhost:1433;databaseName=BT03_Sitemesh_JPA;encrypt=true;trustServerCertificate=true
```

Không gửi project có mật khẩu thật lên GitHub. Trước khi nộp source, thay lại bằng placeholder và cấu hình mật khẩu trên máy demo.

## Bước 3 - Import project vào Spring Tool Suite

### Cách A - Import project mẫu đã làm sẵn (khuyên dùng)

1. Giải nén file ZIP.
2. Mở STS.
3. Chọn `File -> Import`.
4. Chọn `Maven -> Existing Maven Projects`.
5. Nhấn `Next`.
6. Chọn thư mục `BT03_Sitemesh_JPA` vừa giải nén.
7. Đánh dấu project có file `pom.xml`.
8. Nhấn `Finish`.
9. Đợi Maven tải dependency xong.
10. Right click project -> `Maven -> Update Project`.
11. Đánh dấu `Force Update of Snapshots/Releases` -> `OK`.

### Cách B - Tự tạo Maven WAR mới trong STS

Nếu giảng viên yêu cầu quay lại toàn bộ quá trình tạo project, làm như sau:

1. Mở STS.
2. Chọn `File -> New -> Maven Project`.
3. Đánh dấu `Create a simple project (skip archetype selection)`.
4. Nhấn `Next`.
5. Nhập:

```text
Group Id:    vn.edu.hcmute
Artifact Id: BT03_Sitemesh_JPA
Version:     1.0-SNAPSHOT
Packaging:   war
Name:        BT03 Sitemesh JPA
```

6. Nhấn `Finish`.
7. Mở `pom.xml`, chuyển sang tab `pom.xml` hoặc `Text`, rồi thay toàn bộ bằng nội dung `pom.xml` của project mẫu.
8. Right click project -> `Maven -> Update Project` -> đánh dấu Force Update -> `OK`.
9. Tạo lần lượt các source folder nếu STS chưa sinh:

```text
src/main/java
src/main/resources
src/main/webapp
```

10. Trong `src/main/resources`, tạo thư mục `META-INF`, rồi tạo `persistence.xml`.
11. Trong `src/main/java`, tạo các package:

```text
vn.edu.hcmute.controllers
vn.edu.hcmute.dao
vn.edu.hcmute.filters
vn.edu.hcmute.listeners
vn.edu.hcmute.models
vn.edu.hcmute.utils
```

12. Tạo các Java class đúng package và sao chép nội dung từ project mẫu.
13. Trong `src/main/webapp`, tạo:

```text
WEB-INF/decorators
WEB-INF/views
commons/web
assets/css
assets/images
```

14. Sao chép các JSP, CSS, SVG, `web.xml` và `sitemesh3.xml` vào đúng vị trí.
15. Tạo thư mục `database` ở root project và sao chép `create_database.sql`.

Sau khi tạo xong, cấu trúc trong Project Explorer phải giống cấu trúc project mẫu. Không tạo `persistence.xml` trong `webapp`; file này bắt buộc nằm trong `src/main/resources/META-INF`.

## Bước 4 - Cấu hình Tomcat 10.1

1. Mở tab `Servers` trong STS.
2. Chọn `New -> Server`.
3. Chọn `Apache -> Tomcat v10.1 Server`.
4. Chọn thư mục Tomcat 10.1 đã cài.
5. Chọn JRE là JDK 17, 21 hoặc 22.
6. Add project `BT03_Sitemesh_JPA` vào server.
7. Right click server -> `Clean`.
8. Right click server -> `Start`.

Nếu STS báo sai facet:

1. Right click project -> `Properties`.
2. `Project Facets`.
3. Java: chọn 17 trở lên.
4. Dynamic Web Module: chọn 6.0.
5. Runtimes: đánh dấu Tomcat 10.1.

## Bước 5 - Mở ứng dụng

Truy cập:

```text
http://localhost:8080/BT03_Sitemesh_JPA/
```

Tài khoản được tạo tự động khi ứng dụng kết nối database thành công:

```text
Username: demo
Password: Demo@123
```

Có thể đăng nhập bằng email `demo@local.test` với cùng mật khẩu.

## Bước 6 - Kiểm tra SiteMesh

Các file quan trọng:

```text
WEB-INF/web.xml
WEB-INF/sitemesh3.xml
WEB-INF/decorators/web.jsp
commons/web/header.jsp
commons/web/footer.jsp
```

Các trang `home.jsp`, `login.jsp`, `profile.jsp` là content page. Chúng không include header/footer và không import Bootstrap. SiteMesh lấy `title`, `head`, `body` rồi đặt vào `web.jsp`.

Kiểm tra:

- `/home`, `/login`, `/profile` đều có cùng navbar và footer.
- Tiêu đề tab thay đổi theo từng content JSP.
- Header/footer không xuất hiện hai lần.
- CSS và SVG trong `/assets/*` không bị SiteMesh trang trí.

## Bước 7 - Kiểm tra validation

### Login

1. Để trống cả hai trường và nhấn Đăng nhập.
2. Trình duyệt phải báo trường bắt buộc.
3. Tắt JavaScript hoặc gửi request trực tiếp.
4. Server vẫn phải trả lỗi vì `FormValidator.validateLogin()` được gọi trong controller.
5. Khi sai mật khẩu, hệ thống dùng thông báo chung, không tiết lộ username có tồn tại hay không.

### Profile

Thử lần lượt:

- Fullname trống.
- Fullname có chữ số.
- Phone thiếu số hoặc chứa chữ.
- File `.txt` hoặc `.exe`.
- Ảnh lớn hơn 5 MB.
- File đổi đuôi thành `.jpg` nhưng nội dung không phải ảnh.

Tất cả phải bị từ chối trước khi cập nhật database.

## Bước 8 - Kiểm tra profile multipart

1. Đăng nhập `demo / Demo@123`.
2. Chọn menu tài khoản -> `Hồ sơ`.
3. Đổi họ tên.
4. Đổi số điện thoại.
5. Chọn ảnh JPG hoặc PNG nhỏ hơn 5 MB.
6. Nhấn `Lưu thay đổi`.
7. Trang redirect lại `/profile` và hiện thông báo thành công.
8. Logout rồi login lại để xác nhận dữ liệu đã nằm trong database.

Ảnh được lưu ngoài WAR tại:

```text
Windows: C:\Users\<tên-user>\BT03_Sitemesh_JPA\uploads\users
Linux:   /home/<tên-user>/BT03_Sitemesh_JPA/uploads/users
```

Database chỉ lưu tên file UUID trong cột `images`. Vì vậy Clean/Publish Tomcat không làm mất ảnh.

## Luồng chức năng profile

```text
GET /profile
  -> kiểm tra session account
  -> JPA tìm User theo userId
  -> forward profile.jsp
  -> SiteMesh ghép web.jsp

POST /profile multipart
  -> lấy userId từ session
  -> validate fullname, phone, image
  -> lưu ảnh bằng UUID
  -> JPA cập nhật fullname, phone, images
  -> cập nhật session account
  -> redirect GET /profile
```

Không có input `userId` trong form. Người dùng không thể đổi userId trong request để cập nhật tài khoản khác.

## Lỗi thường gặp

### `Login failed for user 'sa'`

- Kiểm tra password trong `persistence.xml`.
- Bật Mixed Mode.
- Kiểm tra tài khoản `sa` không bị disable.

### `The TCP/IP connection to host localhost, port 1433 has failed`

- Bật TCP/IP.
- Kiểm tra SQL Server có nghe port 1433.
- Restart dịch vụ SQL Server.
- Kiểm tra firewall.

### `No Persistence provider for EntityManager named bt03-jpa`

- File phải nằm đúng `src/main/resources/META-INF/persistence.xml`.
- Maven Update Project.
- Kiểm tra Hibernate đã có trong Maven Dependencies.

### `ClassNotFoundException: ConfigurableSiteMeshFilter`

- Maven Update Project với Force Update.
- Clean project.
- Remove/Add lại project khỏi Tomcat.
- Clean Tomcat rồi chạy lại.

### Chỉ thấy nội dung nhưng không thấy navbar/footer

- Kiểm tra `WEB-INF/sitemesh3.xml`.
- Kiểm tra `WEB-INF/decorators/web.jsp`.
- Kiểm tra SiteMesh filter trong `WEB-INF/web.xml`.

### `getPart()` lỗi hoặc trả về null

- Form phải có `method="post"`.
- Form phải có `enctype="multipart/form-data"`.
- Input phải có `name="image"`.
- Controller phải có `@MultipartConfig`.

## Thứ tự quay video nộp bài

1. Trình bày cấu trúc Maven project.
2. Mở dependency SiteMesh trong `pom.xml`.
3. Mở filter trong `web.xml` và mapping trong `sitemesh3.xml`.
4. Mở `web.jsp`, `header.jsp`, `footer.jsp`.
5. Chạy home/login/profile để chứng minh layout dùng chung.
6. Gửi form rỗng để demo validation.
7. Đăng nhập tài khoản demo.
8. Cập nhật fullname và phone.
9. Upload ảnh hợp lệ.
10. Upload file sai để demo validation multipart.
11. Mở bảng `users` trong SSMS để chứng minh JPA đã cập nhật dữ liệu.
