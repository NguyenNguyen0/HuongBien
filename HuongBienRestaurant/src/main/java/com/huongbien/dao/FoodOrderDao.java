package com.huongbien.dao;

import com.huongbien.entity.FoodOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodOrderDao extends GenericDao<FoodOrder> {
    private final CuisineDao cuisineDao;
    private static final FoodOrderDao instance = new FoodOrderDao();

    private FoodOrderDao() {
        super();
        this.cuisineDao = CuisineDao.getInstance();
    }

    public static FoodOrderDao getInstance() {
        return instance;
    }

    @Override
    public FoodOrder resultMapper(ResultSet resultSet) throws SQLException {
        FoodOrder foodOrder = new FoodOrder();
        foodOrder.setFoodOrderId(resultSet.getString("id"));
        foodOrder.setQuantity(resultSet.getInt("quantity"));
        foodOrder.setSalePrice(resultSet.getDouble("salePrice"));
        foodOrder.setNote(resultSet.getString("note"));
        foodOrder.setCuisine(cuisineDao.getById(resultSet.getString("cuisineId")));
        return foodOrder;
    }

    public List<FoodOrder> getAll() {
        return getMany("SELECT id, quantity, salePrice, note, cuisineId FROM FoodOrder");
    }

    public List<FoodOrder> getAllByReservationId(String reservationId) {
        return getMany("SELECT id, quantity, salePrice, note, cuisineId FROM FoodOrder WHERE reservationId = ?", reservationId);
    }

    public FoodOrder getById(String foodOrderId) {
        return getOne("SELECT id, quantity, salePrice, note, cuisineId FROM FoodOrder WHERE id = ?", foodOrderId);
    }

    @Override
    public boolean add(FoodOrder foodOrder) {
        String sql = "INSERT INTO FoodOrder (id, quantity, note, salePrice, cuisineId, reservationId) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = statementHelper.prepareStatement(sql);
            statement.setString(1, foodOrder.getFoodOrderId());
            statement.setInt(2, foodOrder.getQuantity());
            statement.setString(3, foodOrder.getNote());
            statement.setDouble(4, foodOrder.getSalePrice());
            statement.setString(5, foodOrder.getCuisine().getCuisineId());
            statement.setString(6, foodOrder.getFoodOrderId().substring(0, 17));
            int rowAffected = statement.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean add(List<FoodOrder> foodOrders) {
        if (foodOrders.isEmpty()) return false;
        for (FoodOrder foodOrder : foodOrders) {
            if (!add(foodOrder)) {
                return false;
            }
        }
        return true;
    }
}
