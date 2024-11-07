package com.huongbien.dao;

import com.huongbien.entity.FoodOrder;
import com.huongbien.entity.Reservation;
import com.huongbien.entity.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO extends GenericDAO<Reservation> {
    private static final ReservationDAO instance = new ReservationDAO();
    private final CustomerDAO customerDao;
    private final EmployeeDAO employeeDao;
    private final PaymentDAO paymentDao;
    private final TableDAO tableDao;
    private final FoodOrderDAO foodOrderDao;

    private ReservationDAO() {
        super();
        customerDao = CustomerDAO.getInstance();
        employeeDao = EmployeeDAO.getInstance();
        paymentDao = PaymentDAO.getInstance();
        tableDao = TableDAO.getInstance();
        foodOrderDao = FoodOrderDAO.getInstance();
    }

    public static ReservationDAO getInstance() {
        return instance;
    }

    @Override
    public Reservation resultMapper(ResultSet resultSet) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(resultSet.getString("id"));
        reservation.setPartyType(resultSet.getString("partyType"));
        reservation.setPartySize(resultSet.getInt("partySize"));
        reservation.setReservationDate(resultSet.getDate("reservationDate").toLocalDate());
        reservation.setReservationTime(resultSet.getTime("reservationTime").toLocalTime());
        reservation.setReceiveDate(resultSet.getDate("receiveDate").toLocalDate());
        reservation.setStatus(resultSet.getString("status"));
        reservation.setDeposit(resultSet.getDouble("deposit"));
        reservation.setRefundDeposit(resultSet.getDouble("refundDeposit"));
        reservation.setCustomer(customerDao.getById(resultSet.getString("customerId")));
        reservation.setEmployee(employeeDao.getById(resultSet.getString("employeeId")).getFirst());
        reservation.setPayment(paymentDao.getById(resultSet.getString("paymentId")));
        reservation.setTables((ArrayList<Table>) tableDao.getAllByReservationId(reservation.getReservationId()));
        reservation.setFoodOrders((ArrayList<FoodOrder>) foodOrderDao.getAllByReservationId(reservation.getReservationId()));
        return reservation;
    }

    public List<Reservation> getAllByCustomerId(String customerId) {
        return getMany("SELECT * FROM reservation WHERE customerId = ?", customerId);
    }

    public List<Reservation> getAllByEmployeeId(String employeeId) {
        return getMany("SELECT * FROM reservation WHERE employeeId = ?", employeeId);
    }

    public List<Reservation> getAll() {
        return getMany("SELECT * FROM reservation");
    }

    public Reservation getById(String id) {
        return getOne("SELECT * FROM reservation WHERE id = ?", id);
    }

    @Override
    public boolean add(Reservation object) {
        String sql = "INSERT INTO reservation (id, partyType, partySize, reservationDate, reservationTime, receiveDate, status, deposit, refundDeposit, employeeId, customerId, paymentId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = statementHelper.prepareStatement(sql);
            statement.setString(1, object.getReservationId());
            statement.setString(2, object.getPartyType());
            statement.setInt(3, object.getPartySize());
            statement.setDate(4, java.sql.Date.valueOf(object.getReservationDate()));
            statement.setTime(5, java.sql.Time.valueOf(object.getReservationTime()));
            statement.setDate(6, java.sql.Date.valueOf(object.getReceiveDate()));
            statement.setString(7, object.getStatus());
            statement.setDouble(8, object.getDeposit());
            statement.setDouble(9, object.getRefundDeposit());
            statement.setString(10, object.getEmployee().getEmployeeId());
            statement.setString(11, object.getCustomer().getCustomerId());
            statement.setString(12, object.getPayment() == null ? null : object.getPayment().getPaymentId());

            if (object.getPayment() != null) paymentDao.add(object.getPayment());

            int rowAffected = statement.executeUpdate();

            foodOrderDao.add(object.getFoodOrders());
            tableDao.addTablesToReservation(object.getReservationId(), object.getTables());

            return rowAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
