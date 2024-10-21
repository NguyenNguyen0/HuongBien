package com.huongbien.dao;

import com.huongbien.entity.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Reservation_DAO extends Base_DAO<Reservation> {
    private final Connection connection;

    public Reservation_DAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(Reservation object) {
        String sql = "INSERT INTO reservation (id, partyType, partySize, reservationDate, reservationTime, receiveDate, status, deposit, refundDeposit, employeeId, customerId, paymentId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            Payment_DAO paymentDao = new Payment_DAO(connection);
            FoodOrder_DAO foodOrderDao = new FoodOrder_DAO(connection);

            stmt.setString(1, object.getReservationId());
            stmt.setString(2, object.getPartyType());
            stmt.setInt(3, object.getPartySize());
            stmt.setDate(4, java.sql.Date.valueOf(object.getReservationDate()));
            stmt.setTime(5, java.sql.Time.valueOf(object.getReservationTime()));
            stmt.setDate(6, java.sql.Date.valueOf(object.getReceiveDate()));
            stmt.setString(7, object.getStatus());
            stmt.setDouble(8, object.getDeposit());
            stmt.setDouble(9, object.getRefundDeposit());
            stmt.setString(10, object.getEmployee().getEmployeeId());
            stmt.setString(11, object.getCustomer().getCustomerId());
            stmt.setString(12, object.getPayment().getPaymentId());
            paymentDao.add(object.getPayment());

            int rowAffected = stmt.executeUpdate();

            foodOrderDao.add(object.getFoodOrders());

            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//    TODO: cập nhận lại update cho khớp với yêu cầu chương trình
//    Tạm thời không sử dụng phương thức update này
    @Override
    public boolean update(Reservation object) {
        String sql = "UPDATE reservation SET partyType = ?, partySize = ?, reservationDate = ?, reservationTime = ?, receiveDate = ?, status = ?, deposit = ?, refundDeposit = ?, employeeId = ?, customerId = ?, paymentId = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, object.getPartyType());
            stmt.setInt(2, object.getPartySize());
            stmt.setDate(3, java.sql.Date.valueOf(object.getReservationDate()));
            stmt.setTime(4, java.sql.Time.valueOf(object.getReservationTime()));
            stmt.setDate(5, java.sql.Date.valueOf(object.getReceiveDate()));
            stmt.setString(6, object.getStatus());
            stmt.setDouble(7, object.getDeposit());
            stmt.setDouble(8, object.getRefundDeposit());
            stmt.setString(9, object.getEmployee().getEmployeeId());
            stmt.setString(10, object.getCustomer().getCustomerId());
            stmt.setString(11, object.getPayment().getPaymentId());
            stmt.setString(12, object.getReservationId());

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Reservation> get() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT id, partyType, partySize, reservationDate, reservationTime, receiveDate, status, deposit, refundDeposit, customerId, employeeId, paymentId FROM reservation";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            Customer_DAO customerDao = new Customer_DAO(connection);
            Employee_DAO employeeDao = new Employee_DAO(connection);
            Payment_DAO paymentDao = new Payment_DAO(connection);

            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setReservationId(rs.getString("id"));
                reservation.setPartyType(rs.getString("partyType"));
                reservation.setPartySize(rs.getInt("partySize"));
                reservation.setReservationDate(rs.getDate("reservationDate").toLocalDate());
                reservation.setReservationTime(rs.getTime("reservationTime").toLocalTime());
                reservation.setReceiveDate(rs.getDate("receiveDate").toLocalDate());
                reservation.setStatus(rs.getString("status"));
                reservation.setDeposit(rs.getDouble("deposit"));
                reservation.setRefundDeposit(rs.getDouble("refundDeposit"));

                reservation.setCustomer(customerDao.get(rs.getString("customerId")));
                reservation.setEmployee(employeeDao.get(rs.getString("employeeId")));
                reservation.setPayment(paymentDao.get(rs.getString("paymentId")));

                reservations.add(reservation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    @Override
    public Reservation get(String id) {
        Reservation reservation = null;
        String sql = "SELECT id, partyType, partySize, reservationDate, reservationTime, receiveDate, status, deposit, refundDeposit, customerId, employeeId, paymentId FROM reservation WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            Customer_DAO customerDao = new Customer_DAO(connection);
            Employee_DAO employeeDao = new Employee_DAO(connection);
            Payment_DAO paymentDao = new Payment_DAO(connection);

            if (rs.next()) {
                reservation = new Reservation();
                reservation.setReservationId(rs.getString("id"));
                reservation.setPartyType(rs.getString("partyType"));
                reservation.setPartySize(rs.getInt("partySize"));
                reservation.setReservationDate(rs.getDate("reservationDate").toLocalDate());
                reservation.setReservationTime(rs.getTime("reservationTime").toLocalTime());
                reservation.setReceiveDate(rs.getDate("receiveDate").toLocalDate());
                reservation.setStatus(rs.getString("status"));
                reservation.setDeposit(rs.getDouble("deposit"));
                reservation.setRefundDeposit(rs.getDouble("refundDeposit"));

                reservation.setCustomer(customerDao.get(rs.getString("customerId")));
                reservation.setEmployee(employeeDao.get(rs.getString("employeeId")));
                reservation.setPayment(paymentDao.get(rs.getString("paymentId")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservation;
    }

//    public boolean delete(String id) {
//        String sql = "DELETE FROM reservation WHERE id = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, id);
//
//            int rowAffected = stmt.executeUpdate();
//            return rowAffected > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
}
