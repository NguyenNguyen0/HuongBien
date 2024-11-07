package com.huongbien.dao;

import com.huongbien.entity.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDao extends GenericDao<OrderDetail> {
    private final CuisineDao cuisineDao;
    private static final OrderDetailDao instance = new OrderDetailDao();

    private OrderDetailDao() {
        this.cuisineDao = CuisineDao.getInstance();
    }

    public static OrderDetailDao getInstance() {
        return instance;
    }

    @Override
    public OrderDetail resultMapper(ResultSet resultSet) throws SQLException {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDetailId(resultSet.getString("id"));
        orderDetail.setQuantity(resultSet.getInt("quantity"));
        orderDetail.setNote(resultSet.getString("note"));
        orderDetail.setSalePrice(resultSet.getDouble("salePrice"));
        orderDetail.setCuisine(cuisineDao.getById(resultSet.getString("cuisineId")));
        return orderDetail;
    }

    public List<OrderDetail> getAllByOrderId(String orderId) {
        return getMany("SELECT * FROM OrderDetail WHERE orderId = ?", orderId);
    }

    public OrderDetail getById(String id) {
        return getOne("SELECT * FROM OrderDetail WHERE id = ?", id);
    }

    @Override
    public boolean add(OrderDetail object) {
        String sql = "INSERT INTO OrderDetail (id, quantity, note, salePrice, cuisineId, orderId) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = statementHelper.prepareStatement(sql);
            statement.setString(1, object.getOrderDetailId());
            statement.setInt(2, object.getQuantity());
            statement.setString(3, object.getNote());
            statement.setDouble(4, object.getSalePrice());
            statement.setString(5, object.getCuisine().getCuisineId());
            statement.setString(6, object.getOrderDetailId().substring(0, 17));
            int effectedRows = statement.executeUpdate();
            return effectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean add(List<OrderDetail> orderDetails) {
        if (orderDetails.isEmpty()) return false;
        for (OrderDetail orderDetail : orderDetails) {
            if (!add(orderDetail)) {
                return false;
            }
        }
        return true;
    }
}
