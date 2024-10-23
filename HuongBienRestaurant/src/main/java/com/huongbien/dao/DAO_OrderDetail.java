package com.huongbien.dao;

import com.huongbien.entity.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO_OrderDetail extends DAO_Base<OrderDetail> {
    private final Connection connection;

    public DAO_OrderDetail(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(OrderDetail object) {
        String sql = "INSERT INTO OrderDetail (id, quantity, note, salePrice, cuisineId, orderId) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, object.getOrderDetailId());
            stmt.setInt(2, object.getQuantity());
            stmt.setString(3, object.getNote());
            stmt.setDouble(4, object.getSalePrice());
            stmt.setString(5, object.getCuisine().getCuisineId());
            stmt.setString(6, object.getOrderDetailId().substring(0, 17));

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OrderDetail object) {
        String sql = "UPDATE OrderDetail SET quantity = ?, note = ?, salePrice = ?, cuisineId = ?, orderId = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, object.getQuantity());
            stmt.setString(2, object.getNote());
            stmt.setDouble(3, object.getSalePrice());
            stmt.setString(4, object.getCuisine().getCuisineId());
            stmt.setString(5, object.getOrderDetailId().substring(0, 17)); // orderId
            stmt.setString(6, object.getOrderDetailId());

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<OrderDetail> get() {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT id, quantity, note, salePrice, cuisineId FROM OrderDetail";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            DAO_Cuisine cuisineDao = new DAO_Cuisine(connection);

            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderDetailId(rs.getString("id"));
                orderDetail.setQuantity(rs.getInt("quantity"));
                orderDetail.setNote(rs.getString("note"));
                orderDetail.setSalePrice(rs.getDouble("salePrice"));
                orderDetail.setCuisine(cuisineDao.get(rs.getString("cuisineId")));

                orderDetails.add(orderDetail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetails;
    }

    @Override
    public OrderDetail get(String id) {
        OrderDetail orderDetail = null;
        String sql = "SELECT id, quantity, note, salePrice, cuisineId FROM OrderDetail WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            DAO_Cuisine cuisineDao = new DAO_Cuisine(connection);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    orderDetail = new OrderDetail();
                    orderDetail.setOrderDetailId(rs.getString("id"));
                    orderDetail.setQuantity(rs.getInt("quantity"));
                    orderDetail.setNote(rs.getString("note"));
                    orderDetail.setSalePrice(rs.getDouble("salePrice"));
                    orderDetail.setCuisine(cuisineDao.get(rs.getString("cuisineId")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetail;
    }

    public List<OrderDetail> getAllByOrderId (String orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT id, quantity, note, salePrice, cuisineId FROM OrderDetail WHERE orderId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            ResultSet rs = stmt.executeQuery();
            DAO_Cuisine cuisineDao = new DAO_Cuisine(connection);

            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderDetailId(rs.getString("id"));
                orderDetail.setQuantity(rs.getInt("quantity"));
                orderDetail.setNote(rs.getString("note"));
                orderDetail.setSalePrice(rs.getDouble("salePrice"));
                orderDetail.setCuisine(cuisineDao.get(rs.getString("cuisineId")));

                orderDetails.add(orderDetail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetails;
    }

    public boolean add(List<OrderDetail> orderDetails) {
        for (OrderDetail orderDetail : orderDetails) {
            if (!add(orderDetail)) {
                return false;
            }
        }

        return true;
    }
}
