package com.huongbien.dao;

import com.huongbien.entity.FoodOrder;
import com.huongbien.entity.Cuisine;

import java.sql.*;
import java.util.ArrayList;
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
        List<FoodOrder> foodOrders = new ArrayList<>();
        String sql = "SELECT fo.id, fo.quantity, fo.note, fo.salePrice, fo.cuisineId, " +
                "c.name, c.price, c.description, c.image " +
                "FROM FoodOrder fo " +
                "JOIN Cuisine c ON fo.cuisineId = c.id";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FoodOrder foodOrder = new FoodOrder(
                        rs.getDouble("salePrice"),
                        rs.getString("note"),
                        rs.getInt("quantity"),
                        rs.getString("id"),
                        new Cuisine(
                                rs.getString("cuisineId"),
                                rs.getString("name"),
                                rs.getDouble("price"),
                                rs.getString("description"),
                                rs.getBytes("image"),
                                null
                        )
                );
                foodOrders.add(foodOrder);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foodOrders;
    }

    @Override
    public FoodOrder get(String id) {
        FoodOrder foodOrder = null;
        String sql = "SELECT fo.id, fo.quantity, fo.note, fo.salePrice, fo.cuisineId, " +
                "c.name, c.price, c.description, c.image " +
                "FROM FoodOrder fo " +
                "JOIN Cuisine c ON fo.cuisineId = c.id " +
                "WHERE fo.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    foodOrder = new FoodOrder(
                            rs.getDouble("salePrice"),
                            rs.getString("note"),
                            rs.getInt("quantity"),
                            rs.getString("id"),
                            new Cuisine(
                                    rs.getString("cuisineId"),
                                    rs.getString("name"),
                                    rs.getDouble("price"),
                                    rs.getString("description"),
                                    rs.getBytes("image"),
                                    null
                            )
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foodOrder;
    }
}
