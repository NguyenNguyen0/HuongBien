package com.huongbien.dao;

import com.huongbien.entity.Table;

import java.util.List;

public class Table_DAO extends Base_DAO<Table> {
    @Override
    public boolean add(Table object) {
        return false;
    }

    @Override
    public boolean update(Table object) {
        return false;
    }

    @Override
    public List<Table> get() {
        return List.of();
    }

    @Override
    public Table get(String id) {
        return null;
    }
}
