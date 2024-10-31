package com.huongbien.dao;

import com.huongbien.database.Database;
import com.huongbien.entity.Employee;
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

    public boolean update(Table table) {
        String sql = "UPDATE [Table] SET name = ?, seats = ?, floor = ?, status = ?, tableTypeId = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, table.getName());
            statement.setInt(2, table.getSeats());
            statement.setInt(3, table.getFloor());
            statement.setString(4, table.getStatus());
            statement.setString(5, table.getTableType().getTableId());
            statement.setString(6, table.getId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi SQL: " + e.getMessage());
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

    public List<String> getDistinctStatuses() throws SQLException {
        List<String> statuses = new ArrayList<>();
        String sql = "SELECT DISTINCT status FROM [Table]";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String status = resultSet.getString("status");
                if (!"Bàn đóng".equals(status)) {
                    statuses.add(status);
                }
            }
        }
        return statuses;
    }


    public List<String> getDistinctFloor() throws SQLException {
        List<String> floors = new ArrayList<>();
        String sql = "SELECT DISTINCT floor FROM [Table]";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                floors.add(resultSet.getString("floor"));
            }
        }
        return floors;
    }

    public List<Table> getByCriteria(String floor, String status, String typeID) {
        List<Table> tables = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM [Table] WHERE status != N'Bàn đóng'");
        List<String> parameters = new ArrayList<>();

        if (floor != null && !floor.isEmpty()) {
            sqlBuilder.append(" AND floor = ?");
            parameters.add(floor);
        }

        if (status != null && !status.equals("Tất cả trạng thái") && !status.isEmpty()) {
            sqlBuilder.append(" AND status = ?");
            parameters.add(status);
        }

        if (typeID != null && !typeID.equals("Tất cả loại bàn") && !typeID.isEmpty()) {
            sqlBuilder.append(" AND tableTypeId = ?");
            parameters.add(typeID);
        }

        String sql = sqlBuilder.toString();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setString(i + 1, parameters.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Table table = new Table();
                table.setId(rs.getString("id"));
                table.setName(rs.getString("name"));
                table.setSeats(rs.getInt("seats"));
                table.setFloor(rs.getInt("floor"));
                table.setStatus(rs.getString("status"));
                table.setTableType(tableTypeDAO.get(rs.getString("tableTypeId")));
                tables.add(table);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying tables by criteria", e);
        }
        return tables;
    }

}
