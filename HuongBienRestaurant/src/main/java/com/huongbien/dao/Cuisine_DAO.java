package com.huongbien.dao;

import com.huongbien.entity.Cuisine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Cuisine_DAO extends Base_DAO<Cuisine>{
    private final Connection connection;
    private final Category_DAO categoryDao;

    public Cuisine_DAO(Connection connection) {
        this.connection = connection;
        this.categoryDao = new Category_DAO(connection);
    }
    @Override
    public boolean add(Cuisine object) {
        String sql = "INSERT INTO cuisine (id, name, price, description, image, categoryID) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, object.getCuisineId());
            stmt.setString(2, object.getName());
            stmt.setDouble(3, object.getPrice());
            stmt.setString(4, object.getDescription());
            stmt.setBytes(5, object.getImage());
            stmt.setString(6, object.getCategory().getCategoryId());

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Cuisine object) {
        String sql = "UPDATE cuisine SET name = ?, price = ?, description = ?, image = ?, categoryID = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, object.getName());
            stmt.setDouble(2, object.getPrice());
            stmt.setString(3, object.getDescription());
            stmt.setBytes(4, object.getImage());
            stmt.setString(5, object.getCategory().getCategoryId());
            stmt.setString(6, object.getCuisineId());

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public List<Cuisine> get() {
        List<Cuisine> cuisines = new ArrayList<>();
        String sql = "SELECT id, name, price, description, image, categoryId FROM Cuisine";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cuisine cuisine = new Cuisine();
                cuisine.setCuisineId(rs.getString("id"));
                cuisine.setName(rs.getString("name"));
                cuisine.setPrice(rs.getDouble("price"));
                cuisine.setDescription(rs.getString("description"));
                cuisine.setImage(rs.getBytes("image"));
                cuisine.setCategory(categoryDao.get(rs.getString("categoryId")));
                cuisines.add(cuisine);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cuisines;
    }

    @Override
    public Cuisine get(String id) {
        Cuisine cuisine = null;
        String sql = "SELECT id, name, price, description, image, categoryID FROM cuisine WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cuisine = new Cuisine();
                    cuisine.setCuisineId(rs.getString("id"));
                    cuisine.setName(rs.getString("name"));
                    cuisine.setPrice(rs.getDouble("price"));
                    cuisine.setDescription(rs.getString("description"));
                    cuisine.setImage(rs.getBytes("image"));
                    cuisine.setCategory(categoryDao.get(rs.getString("categoryID")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cuisine;
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM cuisine WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
