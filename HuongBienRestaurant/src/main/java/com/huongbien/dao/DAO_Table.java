package com.huongbien.dao;

import com.huongbien.entity.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO_Table extends DAO_Base<Table> {
    private final Connection connection;
    private final DAO_TableType tableTypeDAO;

    public DAO_Table(Connection connection) {
        this.connection = connection;
        this.tableTypeDAO = new DAO_TableType(connection);
    }

    @Override
    public boolean add(Table object) {
        String sql = "INSERT INTO [Table] (id, name, floor, seats, status, tableTypeId) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, object.getId());
            statement.setString(2, object.getName());
            statement.setInt(3, object.getFloor());
            statement.setInt(4, object.getSeats());
            statement.setString(5, object.getStatus());
            statement.setString(6, object.getTableType() != null ? object.getTableType().getTableId() : null);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Table object) {
        String sql = "UPDATE [Table] SET name = ?, floor = ?, seats = ?, status = ?, tableTypeId = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, object.getName());
            statement.setInt(2, object.getFloor());
            statement.setInt(3, object.getSeats());
            statement.setString(4, object.getStatus());
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
                table.setStatus(resultSet.getString("status"));
                table.setTableType(tableTypeDAO.get(resultSet.getString("tableTypeId")));
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
                table.setStatus(resultSet.getString("status"));
                table.setTableType(tableTypeDAO.get(resultSet.getString("tableTypeId")));
                return table;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Table> getByName(String name) {
        List<Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM [Table] WHERE name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Table table = new Table();
                table.setId(resultSet.getString("id"));
                table.setName(resultSet.getString("name"));
                table.setFloor(resultSet.getInt("floor"));
                table.setSeats(resultSet.getInt("seats"));
                table.setStatus(resultSet.getString("status"));
                table.setTableType(tableTypeDAO.get(resultSet.getString("tableTypeId")));
                tables.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public boolean delete(String id) {
        if (id == null || id.isEmpty() || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }

        String sql = "DELETE FROM [Table] WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Table> getAllByReservationId (String reservationId) {
        ArrayList<Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM [Table] WHERE id IN (SELECT tableId FROM Reservation_Table WHERE reservationId = ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reservationId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Table table = new Table();
                table.setId(resultSet.getString("id"));
                table.setName(resultSet.getString("name"));
                table.setFloor(resultSet.getInt("floor"));
                table.setSeats(resultSet.getInt("seats"));
                table.setStatus(resultSet.getString("status"));
                table.setTableType(tableTypeDAO.get(resultSet.getString("tableTypeId")));
                tables.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public List<Table> getAllByOrderId (String orderId) {
        List<Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM [Table] WHERE id IN (SELECT tableId FROM Order_Table WHERE orderId = ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Table table = new Table();
                table.setId(resultSet.getString("id"));
                table.setName(resultSet.getString("name"));
                table.setFloor(resultSet.getInt("floor"));
                table.setSeats(resultSet.getInt("seats"));
                table.setStatus(resultSet.getString("status"));
                table.setTableType(tableTypeDAO.get(resultSet.getString("tableTypeId")));
                tables.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public boolean add(List<Table> tables) {
        for (Table table : tables) {
            if (!add(table)) {
                return false;
            }
        }

        return true;
    }

    public void addTablesToOrder(String orderId, ArrayList<Table> tables) {
        String sql = "INSERT INTO Order_Table (orderId, tableId) VALUES (?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, orderId);

            for (Table table : tables) {
                statement.setString(2, table.getId());
                if (statement.executeUpdate() <= 0) {
                    return;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
