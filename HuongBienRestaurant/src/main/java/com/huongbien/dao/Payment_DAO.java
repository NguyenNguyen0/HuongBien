package com.huongbien.dao;

import com.huongbien.entity.Payment;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Payment_DAO extends Base_DAO<Payment> {
    private final Connection connection;

    public Payment_DAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(Payment payment) {
        String sql = "INSERT INTO Payment (id, amount, paymentDate, paymentMethod, paymentTime) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, payment.getPaymentId());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setDate(3, Date.valueOf(payment.getPaymentDate()));
            pstmt.setString(4, payment.getPaymentMethod());
            pstmt.setTime(5, Time.valueOf(payment.getPaymentTime()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Payment payment) {
        String sql = "UPDATE Payment SET amount = ?, paymentDate = ?, paymentMethod = ?, paymentTime = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, payment.getAmount());
            pstmt.setDate(2, Date.valueOf(payment.getPaymentDate()));
            pstmt.setString(3, payment.getPaymentMethod());
            pstmt.setTime(4, Time.valueOf(payment.getPaymentTime()));
            pstmt.setString(5, payment.getPaymentId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Payment> get() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payment";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String paymentId = rs.getString("id");
                double amount = rs.getDouble("amount");
                LocalDate paymentDate = rs.getDate("paymentDate").toLocalDate();
                String paymentMethod = rs.getString("paymentMethod");
                LocalTime paymentTime = rs.getTime("paymentTime").toLocalTime();
                payments.add(new Payment(paymentId, amount, paymentDate, paymentMethod, paymentTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    @Override
    public Payment get(String id) {
        String sql = "SELECT * FROM Payment WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String paymentId = rs.getString("id");
                    double amount = rs.getDouble("amount");
                    LocalDate paymentDate = rs.getDate("paymentDate").toLocalDate();
                    String paymentMethod = rs.getString("paymentMethod");
                    LocalTime paymentTime = rs.getTime("paymentTime").toLocalTime();
                    return new Payment(paymentId, amount, paymentDate, paymentMethod, paymentTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
