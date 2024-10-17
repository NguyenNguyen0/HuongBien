package com.huongbien.dao;

import com.huongbien.database.Database;
import com.huongbien.entity.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class Category_DAO extends Base_DAO<Category>{
    private Connection connection = null;

    public Category_DAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(Category object) {
        // Assuming you have a database connection named `connection`
        String sql = "INSERT INTO category (categoryId, name, description) VALUES (?, ?, ?)";
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
        return false;
    }

    @Override
    public List<Category> get() {
        return List.of();
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
}
