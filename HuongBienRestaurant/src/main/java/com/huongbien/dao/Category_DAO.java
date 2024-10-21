package com.huongbien.dao;

import com.huongbien.entity.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Category_DAO extends Base_DAO<Category>{
    private final Connection connection;

    public Category_DAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(Category object) {
        String sql = "INSERT INTO category (id, name, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, object.getCategoryId());
            stmt.setString(2, object.getName());
            stmt.setString(3, object.getDescription());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Category object) {
        String sql = "UPDATE category SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, object.getName());
            stmt.setString(2, object.getDescription());
            stmt.setString(3, object.getCategoryId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Category> get() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name, description FROM category";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String categoryId = rs.getString("id");
                String name = rs.getString("name");
                String description = rs.getString("description");

                Category category = new Category(categoryId, name, description);
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }


    @Override
    public Category get(String id) {
        String sql = "SELECT id, name, description FROM category WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String categoryId = rs.getString("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    return new Category(categoryId, name, description);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM category WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
