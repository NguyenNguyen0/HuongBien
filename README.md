# 🍾HuongBien Restaurant🍻

## Mô tả dự án
Dự án **HuongBien Restaurant** là đồ án môn phát triển phần mềm. Là phần mềm quản lý đặt bàn được xây dựng nhằm hỗ trợ hoạt động của nhà hàng Hương Biển. Phần mềm được phát triển với mục tiêu tối ưu hóa quy trình làm việc, nâng cao hiệu suất kinh doanh, giảm thiểu sai sót và cải thiện trải nghiệm khách hàng.

## Danh sách thành viên
- Nguyễn Trần Gia Sĩ **🌟LEADER🌟** (_*giasinguyen Nguyễn Trần Gia Sĩ*_)
- Nguyễn Trung Nguyên (_*NguyenNguyen0 nguyennguyen0*_)
- Nguyễn Văn Minh **👑MVP👑** (_*nvminh162 Nguyen Minh (paul)*_)
- Đào Quốc Tuấn (_*daoquoctuan972*_)

## Các công nghệ được sử dụng
- **Ngôn ngữ lập trình**: Java  
- **Công cụ phát triển**: IntelliJ IDEA  
- **Giao diện người dùng**: JavaFX  
- **Cơ sở dữ liệu**: Microsoft SQL Server  
- **Quản lý dự án**: Git và GitHub  

## Tính năng chính
1. **Quản lý đặt bàn**:
   - Tạo, sửa, hủy đơn đặt bàn.  
   - Kiểm tra trạng thái bàn trống và gợi ý bàn phù hợp.  
2. **Quản lý khách hàng**:
   - Thêm mới, chỉnh sửa thông tin khách hàng.  
   - Theo dõi khách hàng thân thiết và áp dụng ưu đãi.  
3. **Quản lý hóa đơn**:
   - Tính tiền và in hóa đơn.  
   - Tìm kiếm hóa đơn khi cần thiết.  
4. **Thống kê và báo cáo**:
   - Báo cáo doanh thu theo ngày, tuần, tháng.  
   - Thống kê số lượng khách và tình trạng sử dụng bàn.  
5. **Quản lý nhân viên**:
   - Theo dõi thông tin nhân viên.  
   - Quản lý ca làm việc và đánh giá hiệu suất.  

## Cách cài đặt và chạy dự án

### Yêu cầu hệ thống
- **Java**: JDK 23 hoặc mới hơn
- **Javafx**: bản 23 hoặc mới hơn
- **OpenCV**: bản 4.5.1 hoặc mới hơn
- **IntelliJ IDEA**: Bản Community hoặc Ultimate  
- **SQL Server**: SQL Server 2012 hoặc mới hơn
- **Môi trường phát triển**: Windows 10 và Windows 11  

### Hướng dẫn cài đặt
1. Clone repository từ GitHub:  
   ```bash
   git clone <link_repository>

2. Cấu hình file môi trường `.env`
Để dự án hoạt động đúng, bạn cần tạo file `.env` trong thư mục gốc của dự án và thêm các thông tin cấu hình như sau:  

    ```plaintext
    # DATABASE
    DB_URL="jdbc:sqlserver://localhost:1433;databaseName=HuongBien;encrypt=true;trustServerCertificate=true;loginTimeout=30" # for localhost
    DB_USER="" # database username
    DB_PASSWORD="" # database password

    # EMAIL
    EMAIL_USERNAME="restaurant@example.com"
    EMAIL_PASSWORD="xxxx xxxx xxxx xxxx"

3. Cấu hình thư viện opencv
    1. Mở project structure
    ![Mở project structure](HuongBienRestaurant\src\main\resources\com\huongbien\img\readme\open-project-structure.png)
    2. Chọn sửa module opencv
    ![Chọn sửa module opencv](HuongBienRestaurant\src\main\resources\com\huongbien\img\readme\edit-open-cv.png)
    3. Mở đường dẫn đến opencv
    ![Mở đường dẫn đến opencv](HuongBienRestaurant\src\main\resources\com\huongbien\img\readme\open-file-manager.png)
    4. Chọn file jar của java trong opencv
    ![Chọn file jar của java trong opencv](HuongBienRestaurant\src\main\resources\com\huongbien\img\readme\select-dll-file.png)
    5. **Cuối cùng chọn OK và apply**
    ![Cuối cùng chọn OK và apply](HuongBienRestaurant\src\main\resources\com\huongbien\img\readme\apply-change.png)