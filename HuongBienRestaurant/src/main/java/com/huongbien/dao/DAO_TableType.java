package com.huongbien.dao;

import com.huongbien.database.Database;
import com.huongbien.entity.TableType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO_TableType extends DAO_Base<TableType> {
    private final Connection connection;

    public DAO_TableType(Connection connection) {
        this.connection = connection;
    }

    public boolean delete(String id) {
        if (id == null || id.isEmpty() || id.isBlank()) {
            throw new NullPointerException("ID is null");
        }

        String query = "DELETE FROM TableType WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean add(TableType object) {
        if (object == null) {
            throw new NullPointerException("Object is null");
        }

        String query = "INSERT INTO TableType (id, name, description) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, object.getTableId());
            preparedStatement.setString(2, object.getName());
            preparedStatement.setString(3, object.getDescription());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(TableType object) {
        if (object == null) {
            throw new NullPointerException("Object is null");
        }

        String query = "UPDATE TableType SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.setString(3, object.getTableId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<TableType> get() {
        List<TableType> tableTypes = new ArrayList<>();
        String query = "SELECT id, name, description FROM TableType";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String tableId = resultSet.getString("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");

                TableType tableType = new TableType(tableId, name, description);
                tableTypes.add(tableType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tableTypes;
    }

    public TableType get(String id) {
        if (id == null || id.isEmpty() || id.isBlank()) {
            throw new NullPointerException("ID is null");
        }

        String query = "SELECT id, name, description FROM TableType WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String tableId = resultSet.getString("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");

                return new TableType(tableId, name, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public TableType getByName(String name) {
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new NullPointerException("Name is null");
        }

        String query = "SELECT id, name, description FROM TableType WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String tableId = resultSet.getString("id");
                String tableName = resultSet.getString("name");
                String description = resultSet.getString("description");

                return new TableType(tableId, tableName, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getDistinctTableType() throws SQLException {
        List<String> typeList = new ArrayList<>();
        String sql = "SELECT DISTINCT * FROM [TableType]";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                typeList.add(resultSet.getString("name"));
            }
        }
        return typeList;
    }

}
