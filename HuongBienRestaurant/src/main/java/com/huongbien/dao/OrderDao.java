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

public class OrderDao extends GenericDao<Order> {
    private final CustomerDao customerDao;
    private final EmployeeDao employeeDao;
    private final PromotionDao promotionDao;
    private final PaymentDao paymentDao;
    private final OrderDetailDao orderDetailDao;
    private final TableDao tableDao;
    private static final OrderDao instance = new OrderDao();

    private OrderDao() {
        super();
        this.customerDao = CustomerDao.getInstance();
        this.employeeDao = EmployeeDao.getInstance();
        this.promotionDao = PromotionDao.getInstance();
        this.paymentDao = PaymentDao.getInstance();
        this.orderDetailDao = OrderDetailDao.getInstance();
        this.tableDao = TableDao.getInstance();
    }

    public static OrderDao getInstance() {
        return instance;
    }

    @Override
    public Order resultMapper(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setOrderId(resultSet.getString("id"));
        order.setOrderDate(resultSet.getDate("orderDate").toLocalDate());
        order.setNotes(resultSet.getString("notes"));
        order.setPaymentAmount(resultSet.getDouble("paymentAmount"));
        order.setDispensedAmount(resultSet.getDouble("dispensedAmount"));
        order.setTotalAmount(resultSet.getDouble("totalAmount"));
        order.setDiscount(resultSet.getDouble("discount"));

        order.setCustomer(customerDao.getById(resultSet.getString("customerId")));
        order.setEmployee(employeeDao.getById(resultSet.getString("employeeId")).getFirst());
        order.setPromotion(promotionDao.getById(resultSet.getString("promotionId")));
        order.setPayment(paymentDao.getById(resultSet.getString("paymentId")));

        order.setOrderDetails((ArrayList<OrderDetail>) orderDetailDao.getAllByOrderId(order.getOrderId()));
        order.setTables((ArrayList<Table>) tableDao.getAllByOrderId(order.getOrderId()));

        return order;
    }

    public List<Order> getAllByCustomerId(String customerId) {
        return getMany("SELECT * FROM [Order] WHERE customerId = ?", customerId);
    }

    public List<Order> getAllByEmployeeId(String employeeId) {
        return getMany("SELECT * FROM [Order] WHERE employeeId = ?", employeeId);
    }

    public List<Order> getAll() {
        return getMany("SELECT * FROM [Order]");
    }

    public Order getById(String id) {
        return getOne("SELECT * FROM [Order] WHERE id = ?", id);
    }

    @Override
    public boolean add(Order object) {
        String sql = "INSERT INTO [Order] (id, orderDate, notes, vatTax, paymentAmount, dispensedAmount, totalAmount, discount, customerId, employeeId, promotionId, paymentId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = statementHelper.prepareStatement(sql);
            statement.setString(1, object.getOrderId());
            statement.setDate(2, java.sql.Date.valueOf(object.getOrderDate()));
            statement.setString(3, object.getNotes());
            statement.setDouble(4, object.getVatTax());
            statement.setDouble(5, object.getPaymentAmount());
            statement.setDouble(6, object.getDispensedAmount());
            statement.setDouble(7, object.getTotalAmount());
            statement.setDouble(8, object.getDiscount());
            statement.setString(9, object.getCustomer().getCustomerId());
            statement.setString(10, object.getEmployee().getEmployeeId());
            statement.setString(11, object.getPromotion() != null ? object.getPromotion().getPromotionId() : null);
            statement.setString(12, object.getPayment() != null ? object.getPayment().getPaymentId() : null);
            paymentDao.add(object.getPayment());

            int rowAffected = statement.executeUpdate();

            tableDao.addTablesToOrder(object.getOrderId(), object.getTables());
            orderDetailDao.add(object.getOrderDetails());

            return rowAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
