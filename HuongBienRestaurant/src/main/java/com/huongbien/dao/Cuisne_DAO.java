package com.huongbien.dao;

import com.huongbien.entity.Cuisine;

import java.util.List;

public class Cuisne_DAO extends Base_DAO<Cuisine>{

    @Override
    public boolean add(Cuisine object) {
        return false;
    }

    @Override
    public boolean update(Cuisine object) {
        return false;
    }

    @Override
    public List<Cuisine> get() {
        return List.of();
    }

    @Override
    public Cuisine get(String id) {
        return null;
    }
}
