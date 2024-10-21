package com.huongbien.dao;

import com.huongbien.entity.FoodOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodOrder_DAO extends Base_DAO<FoodOrder> {
    private final Connection connection;

    public FoodOrder_DAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(FoodOrder foodOrder) {
        String sql = "INSERT INTO FoodOrder (id, quantity, note, salePrice, cuisineId, reservationId) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, foodOrder.getFoodOrderId());
            stmt.setInt(2, foodOrder.getQuantity());
            stmt.setString(3, foodOrder.getNote());
            stmt.setDouble(4, foodOrder.getSalePrice());
            stmt.setString(5, foodOrder.getCuisine().getCuisineId());
            stmt.setString(6, foodOrder.getFoodOrderId().substring(0, 17));

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

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT id, quantity, salePrice, note, cuisineId FROM FoodOrder");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                FoodOrder foodOrder = new FoodOrder();
                foodOrder.setFoodOrderId(rs.getString("id"));
                foodOrder.setQuantity(rs.getInt("quantity"));
                foodOrder.setSalePrice(rs.getDouble("salePrice"));
                foodOrder.setNote(rs.getString("note"));
                foodOrder.setCuisine(new Cuisine_DAO(connection).get(rs.getString("cuisineId")));
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

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT id, quantity, salePrice, note, cuisineId FROM FoodOrder");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                foodOrder = new FoodOrder();
                foodOrder.setFoodOrderId(rs.getString("id"));
                foodOrder.setQuantity(rs.getInt("quantity"));
                foodOrder.setSalePrice(rs.getDouble("salePrice"));
                foodOrder.setNote(rs.getString("note"));
                foodOrder.setCuisine(new Cuisine_DAO(connection).get(rs.getString("cuisineId")));
                return foodOrder;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foodOrder;
    }

    public List<FoodOrder> getAllByReservationId(String reservationId) {
        List<FoodOrder> foodOrders = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT id, quantity, salePrice, note, cuisineId FROM FoodOrder WHERE reservationId = ?");
            stmt.setString(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                FoodOrder foodOrder = new FoodOrder();
                foodOrder.setFoodOrderId(rs.getString("id"));
                foodOrder.setQuantity(rs.getInt("quantity"));
                foodOrder.setSalePrice(rs.getDouble("salePrice"));
                foodOrder.setNote(rs.getString("note"));
                foodOrder.setCuisine(new Cuisine_DAO(connection).get(rs.getString("cuisineId")));
                foodOrders.add(foodOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foodOrders;
    }

    public boolean add(List<FoodOrder> foodOrders) {
        for (FoodOrder foodOrder : foodOrders) {
            if (!add(foodOrder)) {
                return false;
            }
        }

        return true;
    }
}
