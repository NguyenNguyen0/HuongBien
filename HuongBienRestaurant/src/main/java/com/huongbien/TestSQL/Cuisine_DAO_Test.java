import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CuisineTest {
    private static Connection connection;

    public static void main(String[] args) {
        // Thiết lập kết nối với cơ sở dữ liệu
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
            CuisineDAO cuisineDAO = new CuisineDAO(connection);

            // Tạo một đối tượng Cuisine mới
            Cuisine cuisine = new Cuisine();
            cuisine.setCuisineId("1");
            cuisine.setName("Phở");
            cuisine.setPrice(50000);
            cuisine.setDescription("Món ăn truyền thống Việt Nam");
            cuisine.setImage(new byte[]{ /* Dữ liệu byte của hình ảnh */ });
            Category category = new Category();
            category.setCategoryId("cat1");
            cuisine.setCategory(category);

            // Thực hiện thêm vào cơ sở dữ liệu
            boolean result = cuisineDAO.add(cuisine);
            if (result) {
                System.out.println("Thêm món ăn thành công!");
            } else {
                System.out.println("Thêm món ăn thất bại.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
