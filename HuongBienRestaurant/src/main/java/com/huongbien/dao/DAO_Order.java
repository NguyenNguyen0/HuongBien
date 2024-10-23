package com.huongbien.dao;

import com.huongbien.entity.Order;
import com.huongbien.entity.OrderDetail;
import com.huongbien.entity.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO_Order extends DAO_Base<Order> {
    private final Connection connection;

    public DAO_Order(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(Order object) {
        String sql = "INSERT INTO [Order] (id, orderDate, notes, vatTax, paymentAmount, dispensedAmount, totalAmount, discount, customerId, employeeId, promotionId, paymentId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            DAO_Payment paymentDao = new DAO_Payment(connection);
            DAO_OrderDetail orderDetailDao = new DAO_OrderDetail(connection);
            DAO_Table tableDao = new DAO_Table(connection);

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
            stmt.setString(11, object.getPromotion() != null ? object.getPromotion().getPromotionId() : null);
            stmt.setString(12, object.getPayment() != null ? object.getPayment().getPaymentId() : null);
            paymentDao.add(object.getPayment());

            int rowAffected = stmt.executeUpdate();

            tableDao.addTablesToOrder(object.getOrderId(), object.getTables());
            orderDetailDao.add(object.getOrderDetails());

            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//    TODO: cập nhận lại update cho khớp với yêu cầu chương trình
//    Tạm thời không sử dụng phương thức update này
    @Override
    public boolean update(Order object) {
        String sql = "UPDATE [Order] SET orderDate = ?, notes = ?, vatTax = ?, paymentAmount = ?, dispensedAmount = ?, totalAmount = ?, discount = ?, customerId = ?, employeeId = ?, promotionId = ?, paymentId = ? WHERE id = ?";
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
            stmt.setString(10, object.getPromotion() != null ? object.getPromotion().getPromotionId() : null);
            stmt.setString(11, object.getPayment() != null ? object.getPayment().getPaymentId() : null);
            stmt.setString(12, object.getOrderId());

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
        String sql = "SELECT id, orderDate, notes, vatTax, paymentAmount, dispensedAmount, totalAmount, discount, customerId, employeeId, promotionId, paymentId FROM [Order]";

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
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

    @Override
    public Order get(String id) {
        Order order = null;
        String sql = "SELECT id, orderDate, notes, vatTax, paymentAmount, dispensedAmount, totalAmount, discount, customerId, employeeId, promotionId, paymentId FROM [Order] WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                DAO_Customer customerDao = new DAO_Customer(connection);
                DAO_Employee employeeDao = new DAO_Employee(connection);
                DAO_Promotion promotionDao = new DAO_Promotion(connection);
                DAO_Payment paymentDao = new DAO_Payment(connection);
                DAO_OrderDetail orderDetailDao = new DAO_OrderDetail(connection);
                DAO_Table tableDao = new DAO_Table(connection);

                if (rs.next()) {
                    order = new Order();
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }
}
