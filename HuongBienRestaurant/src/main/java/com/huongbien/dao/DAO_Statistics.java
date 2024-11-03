package com.huongbien.dao;

import com.almasb.fxgl.scene3d.Cone;
import com.huongbien.database.Database;
import com.huongbien.entity.Customer;
import com.huongbien.entity.Order;
import com.huongbien.entity.OrderDetail;
import com.huongbien.entity.Table;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DAO_Statistics {

    // Phương thức lấy tổng doanh thu từ bảng Order theo tiêu chí tháng, quý hoặc năm
    public static double getTotalRevenue(String criteria, int period, int year) {
        String query = switch (criteria) {
            case "Tháng" -> "SELECT SUM(totalAmount) AS total_revenue FROM [HuongBien].[dbo].[Order] " +
                    "WHERE MONTH(orderDate) = ? AND YEAR(orderDate) = ?";
            case "Quý" -> "SELECT SUM(totalAmount) AS total_revenue FROM [HuongBien].[dbo].[Order] " +
                    "WHERE DATEPART(QUARTER, orderDate) = ? AND YEAR(orderDate) = ?";
            case "Năm" -> "SELECT SUM(totalAmount) AS total_revenue FROM [HuongBien].[dbo].[Order] " +
                    "WHERE YEAR(orderDate) = ?";
            default -> "";
        };

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if ("Tháng".equals(criteria) || "Quý".equals(criteria)) {
                stmt.setInt(1, period);
                stmt.setInt(2, year);
            } else if ("Năm".equals(criteria)) {
                stmt.setInt(1, year);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Lấy tổng số hóa đơn từ bảng Order theo tiêu chí tháng, quý hoặc năm
    public static int getTotalInvoices(String criteria, int period, int year) {
        String query = switch (criteria) {
            case "Tháng" -> "SELECT COUNT(id) AS total_invoices FROM [HuongBien].[dbo].[Order] " +
                    "WHERE MONTH(orderDate) = ? AND YEAR(orderDate) = ?";
            case "Quý" -> "SELECT COUNT(id) AS total_invoices FROM [HuongBien].[dbo].[Order] " +
                    "WHERE DATEPART(QUARTER, orderDate) = ? AND YEAR(orderDate) = ?";
            case "Năm" -> "SELECT COUNT(id) AS total_invoices FROM [HuongBien].[dbo].[Order] " +
                    "WHERE YEAR(orderDate) = ?";
            default -> "";
        };

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if ("Tháng".equals(criteria) || "Quý".equals(criteria)) {
                stmt.setInt(1, period);
                stmt.setInt(2, year);
            } else if ("Năm".equals(criteria)) {
                stmt.setInt(1, year);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_invoices");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Phương thức lấy tổng số lượng món ăn từ OrderDetail
    public static int getTotalItemsOrdered(String criteria, int period, int year) {
        String query = switch (criteria) {
            case "Tháng" -> "SELECT SUM(quantity) AS total_items FROM [HuongBien].[dbo].[OrderDetail] OD " +
                    "JOIN [HuongBien].[dbo].[Order] O ON OD.orderId = O.id " +
                    "WHERE MONTH(O.orderDate) = ? AND YEAR(O.orderDate) = ?";
            case "Quý" -> "SELECT SUM(quantity) AS total_items FROM [HuongBien].[dbo].[OrderDetail] OD " +
                    "JOIN [HuongBien].[dbo].[Order] O ON OD.orderId = O.id " +
                    "WHERE DATEPART(QUARTER, O.orderDate) = ? AND YEAR(O.orderDate) = ?";
            case "Năm" -> "SELECT SUM(quantity) AS total_items FROM [HuongBien].[dbo].[OrderDetail] OD " +
                    "JOIN [HuongBien].[dbo].[Order] O ON OD.orderId = O.id " +
                    "WHERE YEAR(O.orderDate) = ?";
            default -> "";
        };

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if ("Tháng".equals(criteria) || "Quý".equals(criteria)) {
                stmt.setInt(1, period);
                stmt.setInt(2, year);
            } else if ("Năm".equals(criteria)) {
                stmt.setInt(1, year);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_items");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    // Các phương thức tính tổng toàn bộ hệ thống
    public static int getTotalCustomers() {
        int totalCustomers = 0;
        String query = "SELECT COUNT(*) AS Total FROM [HuongBien].[dbo].[Customer]";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalCustomers = rs.getInt("Total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCustomers;
    }

    public static double getTotalRevenues() {
        double totalRevenues = 0.0;
        String query = "SELECT SUM(totalAmount) AS Total FROM [HuongBien].[dbo].[Order]";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalRevenues = rs.getDouble("Total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalRevenues;
    }

    public static int getTotalInvoices() {
        int totalInvoices = 0;
        String query = "SELECT COUNT(*) AS Total FROM [HuongBien].[dbo].[Order]";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalInvoices = rs.getInt("Total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalInvoices;
    }

    public static int getTotalReservations() {
        int totalReservations = 0;
        String query = "SELECT COUNT(*) AS Total FROM [HuongBien].[dbo].[Reservation]";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalReservations = rs.getInt("Total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalReservations;
    }

    // Lấy thông tin khác hàng mới trong này hôm nay
    public static List<Customer> getNewCusomterInDay() {
        //        TODO: sửa lại hàm này sau khi tối ưu dao
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer WHERE registrationDate = ?;";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getString("id"));
                customer.setName(rs.getString("name"));
                customer.setAddress(rs.getString("address"));
                customer.setGender(rs.getBoolean("gender"));
                customer.setPhoneNumber(rs.getString("phoneNumber"));
                customer.setEmail(rs.getString("email"));
                customer.setBirthday(rs.getDate("birthday").toLocalDate());
                customer.setRegistrationDate(rs.getDate("registrationDate").toLocalDate());
                customer.setAccumulatedPoints(rs.getInt("accumulatedPoints"));
                customer.setMembershipLevel(rs.getInt("membershipLevel"));
                customers.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    // Lấy các hóa đơn mới lập trong hôm nay
    public static List<Order> getNewOrderInDay() {
        //        TODO: sửa lại hàm này sau khi tối ưu dao
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM [Order] WHERE orderDate = ?;";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = stmt.executeQuery();

            DAO_Customer customerDao = new DAO_Customer(connection);
            DAO_Employee employeeDao = new DAO_Employee(connection);
            DAO_Promotion promotionDao = new DAO_Promotion(connection);
            DAO_Payment paymentDao = new DAO_Payment(connection);
            DAO_OrderDetail orderDetailDao = new DAO_OrderDetail(connection);
            DAO_Table tableDao = new DAO_Table(connection);

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getString("id"));
                order.setOrderDate(rs.getDate("orderDate").toLocalDate());
                order.setNotes(rs.getString("notes"));
                order.setPaymentAmount(rs.getDouble("paymentAmount"));
                order.setDispensedAmount(rs.getDouble("dispensedAmount"));
                order.setTotalAmount(rs.getDouble("totalAmount"));
                order.setDiscount(rs.getDouble("discount"));
                order.setCustomer(customerDao.get(rs.getString("customerId")));
                order.setEmployee(employeeDao.get(rs.getString("employeeId")));
                order.setPromotion(promotionDao.get(rs.getString("promotionId")));
                order.setPayment(paymentDao.get(rs.getString("paymentId")));

                order.setOrderDetails((ArrayList<OrderDetail>) orderDetailDao.getAllByOrderId(order.getOrderId()));
                order.setTables((ArrayList<Table>) tableDao.getAllByOrderId(order.getOrderId()));

                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
}