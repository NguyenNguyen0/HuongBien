package com.huongbien.dao;

import com.huongbien.entity.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Order_DAO extends Base_DAO<Order> {
    private Connection connection = null;

    public Order_DAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(Order object) {
        String sql = "INSERT INTO [Order] (id, orderDate, notes, vatTax, paymentAmount, dispensedAmount, totalAmount, discount, customerId, employeeId, promotionId, paymentId, tableId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, object.getOrderId());
            stmt.setDate(2, java.sql.Date.valueOf(object.getOrderDate()));
            stmt.setString(3, object.getNotes());
            stmt.setDouble(4, object.getVatTax());
            stmt.setDouble(5, object.getPaymentAmount());
            stmt.setDouble(6, object.getDispensedAmount());
            stmt.setDouble(7, object.getTotalAmount());
            stmt.setDouble(8, object.getDiscount());
            stmt.setString(9, object.getCustomer().getCustomerId());
            stmt.setString(10, object.getEmployee().getEmployeeId());
            stmt.setString(11, object.getPromotion() != null ? object.getPromotion().getPromotion_id() : null);
            stmt.setString(12, object.getPayment() != null ? object.getPayment().getPaymentId() : null);
            stmt.setString(13, object.getTables() != null && !object.getTables().isEmpty() ? object.getTables().get(0).getId() : null);

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Order object) {
        String sql = "UPDATE [Order] SET orderDate = ?, notes = ?, vatTax = ?, paymentAmount = ?, dispensedAmount = ?, totalAmount = ?, discount = ?, customerId = ?, employeeId = ?, promotionId = ?, paymentId = ?, tableId = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(object.getOrderDate()));
            stmt.setString(2, object.getNotes());
            stmt.setDouble(3, object.getVatTax());
            stmt.setDouble(4, object.getPaymentAmount());
            stmt.setDouble(5, object.getDispensedAmount());
            stmt.setDouble(6, object.getTotalAmount());
            stmt.setDouble(7, object.getDiscount());
            stmt.setString(8, object.getCustomer().getCustomerId());
            stmt.setString(9, object.getEmployee().getEmployeeId());
            stmt.setString(10, object.getPromotion() != null ? object.getPromotion().getPromotion_id() : null);
            stmt.setString(11, object.getPayment() != null ? object.getPayment().getPaymentId() : null);
            stmt.setString(12, object.getTables() != null && !object.getTables().isEmpty() ? object.getTables().get(0).getId() : null); // Chỉ lấy ID của bàn đầu tiên
            stmt.setString(13, object.getOrderId());

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Order> get() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, orderDate, notes, vatTax, paymentAmount, dispensedAmount, totalAmount, discount, customerId, employeeId, promotionId, paymentId, tableId FROM [Order]";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getString("id"));
                order.setOrderDate(rs.getDate("orderDate").toLocalDate());
                order.setNotes(rs.getString("notes"));
                order.setPaymentAmount(rs.getDouble("paymentAmount"));
                order.setDispensedAmount(rs.getDouble("dispensedAmount"));
                order.setTotalAmount(rs.getDouble("totalAmount"));
                order.setDiscount(rs.getDouble("discount"));

                String customerId = rs.getString("customerId");
                order.setCustomer(new Customer_DAO(connection).get(customerId));

                String employeeId = rs.getString("employeeId");
                order.setEmployee(new Employee_DAO(connection).get(employeeId));

                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public Order get(String id) {
        Order order = null;
        String sql = "SELECT id, orderDate, notes, vatTax, paymentAmount, dispensedAmount, totalAmount, discount, customerId, employeeId, promotionId, paymentId, tableId FROM [Order] WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    order = new Order();
                    order.setOrderId(rs.getString("id"));
                    order.setOrderDate(rs.getDate("orderDate").toLocalDate());
                    order.setNotes(rs.getString("notes"));
                    order.setPaymentAmount(rs.getDouble("paymentAmount"));
                    order.setDispensedAmount(rs.getDouble("dispensedAmount"));
                    order.setTotalAmount(rs.getDouble("totalAmount"));
                    order.setDiscount(rs.getDouble("discount"));
                    String customerId = rs.getString("customerId");
                    order.setCustomer(new Customer_DAO(connection).get(customerId));
                    String employeeId = rs.getString("employeeId");
                    order.setEmployee(new Employee_DAO(connection).get(employeeId));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }
}
