package com.huongbien.dao;

import com.huongbien.entity.TableType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TableTypeDao extends GenericDao<TableType> {
    private static final TableTypeDao instance = new TableTypeDao();

    private TableTypeDao() {
        super();
    }

    public static TableTypeDao getInstance() {
        return instance;
    }

    @Override
    public TableType resultMapper(ResultSet resultSet) throws SQLException {
        TableType tableType = new TableType();
        tableType.setTableId(resultSet.getString("id"));
        tableType.setName(resultSet.getString("name"));
        tableType.setDescription(resultSet.getString("description"));
        return tableType;
    }

    public List<TableType> getAll() {
        return getMany("SELECT * FROM TableType;");
    }

    public TableType getById(String id) {
        return getOne("SELECT * FROM TableType WHERE id = ?;", id);
    }

    @Override
    public boolean add(TableType object) {
        try {
            String query = "INSERT INTO TableType (id, name, description) VALUES (?, ?, ?)";
            PreparedStatement statement = statementHelper.prepareStatement(query, object.getTableId(), object.getName(), object.getDescription());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(String id) {
        try {
            String query = "DELETE FROM TableType WHERE id = ?";
            PreparedStatement statement = statementHelper.prepareStatement(query, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
