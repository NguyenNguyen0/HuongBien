package com.huongbien.dao;

import com.huongbien.entity.OrderDetail;
import com.huongbien.entity.Cuisine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetail_DAO extends Base_DAO<OrderDetail> {
    private Connection connection = null;

    public OrderDetail_DAO(Connection connection) {
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
            stmt.setString(6, object.getOrderDetailId());

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
            stmt.setString(5, object.getOrderDetailId()); 
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
        String sql = "SELECT od.*, c.id AS cuisineId, c.name AS cuisineName FROM OrderDetail od LEFT JOIN Cuisine c ON od.cuisineId = c.id";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cuisine cuisine = new Cuisine();
                cuisine.setCuisineId(rs.getString("cuisineId"));
                cuisine.setName(rs.getString("cuisineName"));
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderDetailId(rs.getString("id"));
                orderDetail.setQuantity(rs.getInt("quantity"));
                orderDetail.setNote(rs.getString("note"));
                orderDetail.setSalePrice(rs.getDouble("salePrice"));
                orderDetail.setCuisine(cuisine);
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
        String sql = "SELECT od.*, c.id AS cuisineId, c.name AS cuisineName FROM OrderDetail od LEFT JOIN Cuisine c ON od.id = ? AND od.cuisineId = c.id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cuisine cuisine = new Cuisine();
                    cuisine.setCuisineId(rs.getString("cuisineId"));
                    cuisine.setName(rs.getString("cuisineName"));
                    orderDetail = new OrderDetail();
                    orderDetail.setOrderDetailId(rs.getString("id"));
                    orderDetail.setQuantity(rs.getInt("quantity"));
                    orderDetail.setNote(rs.getString("note"));
                    orderDetail.setSalePrice(rs.getDouble("salePrice"));
                    orderDetail.setCuisine(cuisine);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetail;
    }
}
