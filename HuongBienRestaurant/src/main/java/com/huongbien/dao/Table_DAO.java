package com.huongbien.dao;

import com.huongbien.entity.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Table_DAO extends Base_DAO<Table> {
    private Connection connection;

    public Table_DAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(Table object) {
        String sql = "INSERT INTO [Table] (id, name, floor, seats, isAvailable, tableTypeId) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, object.getId());
            statement.setString(2, object.getName());
            statement.setInt(3, object.getFloor());
            statement.setInt(4, object.getSeats());
            statement.setBoolean(5, object.getIsAvailable());
            statement.setString(6, object.getTableType() != null ? object.getTableType().getTableId() : null);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Table object) {
        String sql = "UPDATE [Table] SET name = ?, floor = ?, seats = ?, isAvailable = ?, tableTypeId = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, object.getName());
            statement.setInt(2, object.getFloor());
            statement.setInt(3, object.getSeats());
            statement.setBoolean(4, object.getIsAvailable());
            statement.setString(5, object.getTableType() != null ? object.getTableType().getTableId() : null);
            statement.setString(6, object.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Table> get() {
        List<Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM [Table]";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Table table = new Table();
                table.setId(resultSet.getString("id"));
                table.setName(resultSet.getString("name"));
                table.setFloor(resultSet.getInt("floor"));
                table.setSeats(resultSet.getInt("seats"));
                table.setIsAvailable(resultSet.getBoolean("isAvailable"));
                // Load table type if necessary
                tables.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    @Override
    public Table get(String id) {
        String sql = "SELECT * FROM [Table] WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Table table = new Table();
                table.setId(resultSet.getString("id"));
                table.setName(resultSet.getString("name"));
                table.setFloor(resultSet.getInt("floor"));
                table.setSeats(resultSet.getInt("seats"));
                table.setIsAvailable(resultSet.getBoolean("isAvailable"));
                // Load table type if necessary
                return table;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
