package com.huongbien.dao;

import com.huongbien.entity.TableType;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.List;

public class TableType_DAO extends Base_DAO<TableType> {
    private Connection connection = null;

    public TableType_DAO(Connection connection) {
        this.connection = connection;
    }

    public boolean delete(String id) {
        if (id.isEmpty() || id.isBlank()) {
            throw new NullPointerException("ID is null");
        }

        return false;
    }

    @Override
    public boolean add(TableType object) {
        if (object == null) {
            throw new NullPointerException("Object is null");
        }

        return false;
    }

    @Override
    public boolean update(TableType object) {
        if (object == null) {
            throw new NullPointerException("Object is null");
        }

        return false;
    }

    @Override
    public List<TableType> get() {
        return null;
    }

    public TableType get(String id) {
        return null;
    }
}
