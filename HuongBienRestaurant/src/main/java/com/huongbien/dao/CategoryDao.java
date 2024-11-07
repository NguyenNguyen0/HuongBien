package com.huongbien.dao;

import com.huongbien.entity.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class CategoryDao extends GenericDao<Category> {
    private static final CategoryDao instance = new CategoryDao();

    private CategoryDao() {
        super();
    }

    public static CategoryDao getInstance() {
        return instance;
    }

    @Override
    public Category resultMapper(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setCategoryId(resultSet.getString("id"));
        category.setName(resultSet.getString("name"));
        category.setDescription(resultSet.getString("description"));
        return category;
    }

    public List<Category> getAll() {
        return getMany("SELECT id, name, description FROM category");
    }

    public List<Category> getByName(String name) {
        return getMany("SELECT id, name, description FROM category WHERE name LIKE ?", "%" + name + "%");
    }

    public Category getById(String id) {
        return getOne("SELECT id, name, description FROM category WHERE id = ?", id);
    }

    @Override
    public boolean add(Category object) {
        String sql = "INSERT INTO category (id, name, description) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = statementHelper.prepareStatement(sql, object.getCategoryId(), object.getName(), object.getDescription());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM category WHERE id = ?";
        try {
            PreparedStatement statement = statementHelper.prepareStatement(sql, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
