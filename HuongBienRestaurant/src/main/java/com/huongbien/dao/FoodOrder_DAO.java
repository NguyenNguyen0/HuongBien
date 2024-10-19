package com.huongbien.dao;

import com.huongbien.entity.FoodOrder;
import com.huongbien.entity.Cuisine;

import java.sql.*;
import java.util.List;

public class FoodOrder_DAO extends Base_DAO<FoodOrder> {
    private Connection connection;

    public FoodOrder_DAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(FoodOrder foodOrder) {
        String sql = "INSERT INTO FoodOrder (id, quantity, note, salePrice, cuisineId) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, foodOrder.getFoodOrderId());
            stmt.setInt(2, foodOrder.getQuantity());
            stmt.setString(3, foodOrder.getNote());
            stmt.setDouble(4, foodOrder.getSalePrice());
            stmt.setString(5, foodOrder.getCuisine().getCuisineId());

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding FoodOrder: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(FoodOrder foodOrder) {
        String sql = "UPDATE FoodOrder SET quantity = ?, note = ?, salePrice = ?, cuisineId = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, foodOrder.getQuantity());
            stmt.setString(2, foodOrder.getNote());
            stmt.setDouble(3, foodOrder.getSalePrice());
            stmt.setString(4, foodOrder.getCuisine().getCuisineId());
            stmt.setString(5, foodOrder.getFoodOrderId());

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<FoodOrder> get() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public FoodOrder get(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

}
