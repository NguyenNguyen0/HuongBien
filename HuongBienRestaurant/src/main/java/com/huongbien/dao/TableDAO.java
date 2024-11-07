package com.huongbien.dao;

import com.huongbien.entity.Table;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TableDAO extends GenericDAO<Table> {
    private final TableTypeDAO tableTypeDAO;
    private static final TableDAO instance = new TableDAO();

    private TableDAO() {
        super();
        this.tableTypeDAO = TableTypeDAO.getInstance();
    }

    public static TableDAO getInstance() {
        return instance;
    }

    @Override
    public Table resultMapper(ResultSet resultSet) throws SQLException {
        Table table = new Table();
        table.setId(resultSet.getString("id"));
        table.setName(resultSet.getString("name"));
        table.setFloor(resultSet.getInt("floor"));
        table.setSeats(resultSet.getInt("seats"));
        table.setStatus(resultSet.getString("status"));
        table.setTableType(tableTypeDAO.getById(resultSet.getString("tableTypeId")));
        return table;
    }

    public List<Table> getAll() {
        return getMany("SELECT * FROM [Table];");
    }

    public Table getById(String id) {
        return getOne("SELECT * FROM [Table] WHERE id = ?", id);
    }

    public List<Table> getByName(String name) {
        return getMany("SELECT * FROM [Table] WHERE name LIKE ?", name + "%");
    }

    public List<Table> getAllByReservationId(String reservationId) {
        return getMany("SELECT * FROM [Table] WHERE id IN (SELECT tableId FROM Reservation_Table WHERE reservationId = ?)", reservationId);
    }

    public List<Table> getAllByOrderId(String orderId) {
        return getMany("SELECT * FROM [Table] WHERE id IN (SELECT tableId FROM Order_Table WHERE orderId = ?)", orderId);
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

        try {
            PreparedStatement stmt = statementHelper.prepareStatement(sql);
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setString(i + 1, parameters.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tables.add(resultMapper(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying tables by criteria", e);
        }
        return tables;
    }

    public List<String> getDistinctFloor() {
        List<String> floors = new ArrayList<>();
        String sql = "SELECT DISTINCT floor FROM [Table]";

        try {
            PreparedStatement statement = statementHelper.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                floors.add(resultSet.getString("floor"));
            }
            return floors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Table> getReservedTables(LocalDate receiveDate) {
        try {
            CallableStatement procedure = statementHelper.callProcedure("{call GetReservedTable(?)}", receiveDate);
            ResultSet resultSet = procedure.executeQuery();
            List<Table> tables = new ArrayList<>();
            while (resultSet.next()) {
                tables.add(resultMapper(resultSet));
            }
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getDistinctStatuses() {
        try {
            PreparedStatement statement = statementHelper.prepareStatement("SELECT DISTINCT status FROM [Table]");
            ResultSet resultSet = statement.executeQuery();
            List<String> statuses = new ArrayList<>();
            while (resultSet.next()) {
                statuses.add(resultSet.getString("status"));
            }
            return statuses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateStatus(String tableId, String status) {
        return update("UPDATE [Table] SET status = ? WHERE id = ?", status, tableId);
    }

    @Override
    public boolean add(Table object) {
        try {
            String sql = "INSERT INTO [Table] (id, name, floor, seats, status, tableTypeId) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = statementHelper.prepareStatement(sql, object.getId(), object.getName(), object.getFloor(), object.getSeats(), object.getStatus(), object.getTableType() != null ? object.getTableType().getTableId() : null);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateTableInfo(Table table) {
        String sql = "UPDATE [Table] SET name = ?, floor = ?, seats = ?, status = ?, tableTypeId = ? WHERE id = ?";
        return update(sql, table.getName(), table.getFloor(), table.getSeats(), table.getStatus(), table.getTableType().getTableId(), table.getId());
    }

    public boolean add(List<Table> tables) throws SQLException {
        for (Table table : tables) {
            if (!add(table)) {
                return false;
            }
        }
        return true;
    }

    public boolean addTablesToOrder(String orderId, List<Table> tables) {
        try {
            String sql = "INSERT INTO Order_Table (orderId, tableId) VALUES (?, ?)";
            PreparedStatement statement = statementHelper.prepareStatement(sql, orderId);
            for (Table table : tables) {
                statement.setString(2, table.getId());
                if (statement.executeUpdate() <= 0) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addTablesToReservation(String reservationId, List<Table> tables) {
        try {
            String sql = "INSERT INTO Reservation_Table (reservationId, tableId) VALUES (?, ?)";
            PreparedStatement statement = statementHelper.prepareStatement(sql, reservationId);
            for (Table table : tables) {
                statement.setString(2, table.getId());
                if (statement.executeUpdate() <= 0) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(String id) {
        try {
            String sql = "DELETE FROM [Table] WHERE id = ?";
            PreparedStatement statement = statementHelper.prepareStatement(sql, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
