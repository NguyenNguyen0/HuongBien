package com.huongbien.dao;

import com.huongbien.entity.Payment;

import java.sql.*;
import java.util.List;

public class PaymentDao extends GenericDao<Payment> {
    private static final PaymentDao instance = new PaymentDao();

    private PaymentDao() {
        super();
    }

    public static PaymentDao getInstance() {
        return instance;
    }

    @Override
    public Payment resultMapper(ResultSet resultSet) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(resultSet.getString("id"));
        payment.setAmount(resultSet.getDouble("amount"));
        payment.setPaymentDate(resultSet.getDate("paymentDate").toLocalDate());
        payment.setPaymentTime(resultSet.getTime("paymentTime").toLocalTime());
        payment.setPaymentMethod(resultSet.getString("paymentMethod"));
        return payment;
    }

    public List<Payment> getAll() {
        return getMany("SELECT * FROM Payment");
    }

    public Payment getById(String id) {
        return getOne("SELECT * FROM Payment WHERE id = ?", id);
    }

    @Override
    public boolean add(Payment payment) {
        try {
            String sql = "INSERT INTO Payment (id, amount, paymentDate, paymentMethod, paymentTime) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = statementHelper.prepareStatement(sql);
            statement.setString(1, payment.getPaymentId());
            statement.setDouble(2, payment.getAmount());
            statement.setDate(3, Date.valueOf(payment.getPaymentDate()));
            statement.setString(4, payment.getPaymentMethod());
            statement.setTime(5, Time.valueOf(payment.getPaymentTime()));
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
