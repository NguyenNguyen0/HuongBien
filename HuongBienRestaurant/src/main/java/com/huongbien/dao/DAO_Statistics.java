package com.huongbien.dao;

import com.huongbien.database.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAO_Statistics {

    // Phương thức lấy tổng doanh thu từ bảng Order theo tiêu chí tháng, quý hoặc năm
    public static double getTotalRevenue(String criteria, int period, int year) {
        String query = "";

        switch (criteria) {
            case "Tháng":
                query = "SELECT SUM(totalAmount) AS total_revenue FROM [HuongBien].[dbo].[Order] " +
                        "WHERE MONTH(orderDate) = ? AND YEAR(orderDate) = ?";
                break;
            case "Quý":
                query = "SELECT SUM(totalAmount) AS total_revenue FROM [HuongBien].[dbo].[Order] " +
                        "WHERE DATEPART(QUARTER, orderDate) = ? AND YEAR(orderDate) = ?";
                break;
            case "Năm":
                query = "SELECT SUM(totalAmount) AS total_revenue FROM [HuongBien].[dbo].[Order] " +
                        "WHERE YEAR(orderDate) = ?";
                break;
        }

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
        String query = "";

        switch (criteria) {
            case "Tháng":
                query = "SELECT COUNT(id) AS total_invoices FROM [HuongBien].[dbo].[Order] " +
                        "WHERE MONTH(orderDate) = ? AND YEAR(orderDate) = ?";
                break;
            case "Quý":
                query = "SELECT COUNT(id) AS total_invoices FROM [HuongBien].[dbo].[Order] " +
                        "WHERE DATEPART(QUARTER, orderDate) = ? AND YEAR(orderDate) = ?";
                break;
            case "Năm":
                query = "SELECT COUNT(id) AS total_invoices FROM [HuongBien].[dbo].[Order] " +
                        "WHERE YEAR(orderDate) = ?";
                break;
        }

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
        String query = "";

        switch (criteria) {
            case "Tháng":
                query = "SELECT SUM(quantity) AS total_items FROM [HuongBien].[dbo].[OrderDetail] OD " +
                        "JOIN [HuongBien].[dbo].[Order] O ON OD.orderId = O.id " +
                        "WHERE MONTH(O.orderDate) = ? AND YEAR(O.orderDate) = ?";
                break;
            case "Quý":
                query = "SELECT SUM(quantity) AS total_items FROM [HuongBien].[dbo].[OrderDetail] OD " +
                        "JOIN [HuongBien].[dbo].[Order] O ON OD.orderId = O.id " +
                        "WHERE DATEPART(QUARTER, O.orderDate) = ? AND YEAR(O.orderDate) = ?";
                break;
            case "Năm":
                query = "SELECT SUM(quantity) AS total_items FROM [HuongBien].[dbo].[OrderDetail] OD " +
                        "JOIN [HuongBien].[dbo].[Order] O ON OD.orderId = O.id " +
                        "WHERE YEAR(O.orderDate) = ?";
                break;
        }

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


}
